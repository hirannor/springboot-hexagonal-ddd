package hu.hirannor.hexagonal.application.service.basket;

import hu.hirannor.hexagonal.application.service.basket.error.BasketNotFound;
import hu.hirannor.hexagonal.application.usecase.basket.*;
import hu.hirannor.hexagonal.domain.basket.*;
import hu.hirannor.hexagonal.domain.basket.command.*;
import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.domain.product.*;
import hu.hirannor.hexagonal.infrastructure.application.ApplicationService;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationService
class BasketCommandService implements
        BasketCreation,
        BasketDeletion,
        BasketCheckout,
        BasketProductHandling {

    private final BasketRepository baskets;
    private final ProductRepository products;
    private final BiFunction<Basket, Map<ProductId, Product>, BasketView> mapBasketToView;


    @Autowired
    BasketCommandService(final BasketRepository baskets, final ProductRepository products) {
        this.baskets = baskets;
        this.products = products;
        this.mapBasketToView = new BasketToViewMapper();
    }

    @Override
    public Basket create(final CreateBasket creation) {
        if (creation == null) throw new IllegalArgumentException("CreateBasket is null");

        final Basket basket = Basket.create(creation);
        baskets.save(basket);

        return basket;
    }

    @Override
    public void deleteBy(final CustomerId customer) {
        if (customer == null) throw new IllegalArgumentException("CustomerId is null");

        baskets.deleteBy(customer);
    }

    @Override
    public BasketView checkout(final CheckoutBasket command) {
        if (command == null) throw new IllegalArgumentException("CheckoutBasket is null");

        final Basket basket = baskets.findBy(command.customerId())
                .orElseThrow(failBecauseBasketWasNotFoundBy(command.customerId()));

        basket.checkout();
        baskets.save(basket);

        return mapToView(basket);
    }

    @Override
    public void add(final AddBasketItem command) {
        if (command == null) throw new IllegalArgumentException("AddBasketItem is null");

        final Basket basket = baskets.findBy(command.basketId())
                .orElseThrow(failBecauseBasketWasNotFoundBy(command.basketId()));

        basket.addProduct(command.item());
        baskets.save(basket);
    }

    @Override
    public void remove(final RemoveBasketItem command) {
        final Basket basket = baskets.findBy(command.basketId())
                .orElseThrow(failBecauseBasketWasNotFoundBy(command.basketId()));

        basket.removeProduct(command.item());
        baskets.save(basket);
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
