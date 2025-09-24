package io.github.hirannor.oms.application.service.basket;

import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.application.service.basket.error.BasketNotFound;
import io.github.hirannor.oms.application.service.customer.error.CustomerNotFound;
import io.github.hirannor.oms.application.usecase.basket.BasketCheckout;
import io.github.hirannor.oms.application.usecase.basket.BasketCreation;
import io.github.hirannor.oms.application.usecase.basket.BasketDeletion;
import io.github.hirannor.oms.application.usecase.basket.BasketProductHandling;
import io.github.hirannor.oms.domain.basket.Basket;
import io.github.hirannor.oms.domain.basket.BasketId;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.basket.BasketRepository;
import io.github.hirannor.oms.domain.basket.command.AddBasketItem;
import io.github.hirannor.oms.domain.basket.command.CheckoutBasket;
import io.github.hirannor.oms.domain.basket.command.CreateBasket;
import io.github.hirannor.oms.domain.basket.command.RemoveBasketItem;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.customer.CustomerRepository;
import io.github.hirannor.oms.domain.inventory.Inventory;
import io.github.hirannor.oms.domain.inventory.InventoryRepository;
import io.github.hirannor.oms.domain.product.Product;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.domain.product.ProductRepository;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApplicationService
class BasketCommandService implements
        BasketCreation,
        BasketDeletion,
        BasketCheckout,
        BasketProductHandling {

    private static final Logger LOGGER = LogManager.getLogger(
            BasketCommandService.class
    );

    private final BasketRepository baskets;
    private final CustomerRepository customers;
    private final ProductRepository products;
    private final Outbox outboxes;
    private final InventoryRepository inventories;
    private final BiFunction<Basket, Map<ProductId, Product>, BasketView> mapBasketToView;


    @Autowired
    BasketCommandService(final BasketRepository baskets,
                         final CustomerRepository customers,
                         final ProductRepository products,
                         final Outbox outboxes,
                         final InventoryRepository inventories) {
        this.baskets = baskets;
        this.customers = customers;
        this.products = products;
        this.outboxes = outboxes;
        this.inventories = inventories;
        this.mapBasketToView = new BasketToViewMapper();
    }

    @Override
    public Basket create(final CreateBasket creation) {
        if (creation == null) throw new IllegalArgumentException("CreateBasket is null");

        LOGGER.info("Start creating basket for customerId={}", creation.customerId().asText());

        customers.findBy(creation.customerId())
                .orElseThrow(failBecauseCustomerWasNotFoundBy(creation.customerId()));

        final Basket basket = Basket.create(creation);
        baskets.save(basket);

        basket.events()
                .forEach(outboxes::save);
        basket.clearEvents();

        LOGGER.info("Created basket with basketId={} for customerId={}",
                basket.id().asText(), creation.customerId().asText());

        return basket;
    }

    @Override
    public void deleteBy(final CustomerId customer) {
        if (customer == null) throw new IllegalArgumentException("CustomerId is null");

        baskets.deleteBy(customer);
        LOGGER.info("Basket is deleted for customerId={}", customer.asText());

    }

    @Override
    public BasketView checkout(final CheckoutBasket command) {
        if (command == null) throw new IllegalArgumentException("CheckoutBasket is null");

        LOGGER.info("Start check-out basket for customerId={}", command.customerId().asText());

        final Basket basket = baskets.findBy(command.customerId())
                .orElseThrow(failBecauseBasketWasNotFoundBy(command.customerId()));

        basket.checkout();
        baskets.save(basket);

        basket.events()
                .forEach(outboxes::save);
        basket.clearEvents();

        final Map<ProductId, BasketItem> itemsByProduct = basket.items()
                .stream()
                .collect(Collectors.toMap(BasketItem::productId, item -> item));

        final List<ProductId> productIds = itemsByProduct.keySet().stream().toList();
        final List<Inventory> inventoriesList = inventories.findAllBy(productIds);

        for (final Inventory inv : inventoriesList) {
            final BasketItem item = itemsByProduct.get(inv.productId());

            inv.reserve(item.quantity());
            inventories.save(inv);

            inv.events().forEach(outboxes::save);
            inv.clearEvents();
        }

        LOGGER.info("Basket with basketId={} is successfully checked out for customerId={}",
                basket.id().asText(),
                basket.customer().asText()
        );

        return mapToView(basket);
    }

    @Override
    public void add(final AddBasketItem command) {
        if (command == null) throw new IllegalArgumentException("AddBasketItem is null");

        LOGGER.info("Start adding productId={} to basketId={}",
                command.item().productId().asText(),
                command.basketId().asText());

        final Basket basket = baskets.findBy(command.basketId())
                .orElseThrow(failBecauseBasketWasNotFoundBy(command.basketId()));

        basket.addProduct(command.item());
        baskets.save(basket);

        basket.events()
                .forEach(outboxes::save);
        basket.clearEvents();

        LOGGER.info("Product with productId={} added to basketId={}",
                command.item().productId().asText(),
                command.basketId().asText()
        );
    }

    @Override
    public void remove(final RemoveBasketItem command) {
        if (command == null) throw new IllegalArgumentException("RemoveBasketItem is null");

        LOGGER.info("Start removing productId={} from basketId={}",
                command.item().productId().asText(),
                command.basketId().asText()
        );


        final Basket basket = baskets.findBy(command.basketId())
                .orElseThrow(failBecauseBasketWasNotFoundBy(command.basketId()));

        basket.removeProduct(command.item());
        baskets.save(basket);

        basket.events()
                .forEach(outboxes::save);
        basket.clearEvents();


        LOGGER.info("Product with productId={} removed from basketId={}",
                command.item().productId().asText(),
                command.basketId().asText()
        );
    }

    private Supplier<CustomerNotFound> failBecauseCustomerWasNotFoundBy(final CustomerId id) {
        return () -> new CustomerNotFound("Customer not found with id:" + id.asText());
    }

    private static Supplier<BasketNotFound> failBecauseBasketWasNotFoundBy(final CustomerId customerId) {
        return () -> new BasketNotFound("Basket with customer id " + customerId + " not found");
    }

    private static Supplier<BasketNotFound> failBecauseBasketWasNotFoundBy(final BasketId basketId) {
        return () -> new BasketNotFound("Basket with id " + basketId + " not found");
    }

    private BasketView mapToView(final Basket basket) {
        final List<ProductId> productIds = basket.items().stream()
                .map(BasketItem::productId)
                .toList();

        final Map<ProductId, Product> productMap = products.findAllBy(productIds)
                .stream()
                .collect(Collectors.toMap(Product::id, p -> p));

        return mapBasketToView.apply(basket, productMap);
    }
}
