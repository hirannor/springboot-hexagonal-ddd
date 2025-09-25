package io.github.hirannor.oms.application.service.order;

import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.application.service.basket.error.BasketNotFound;
import io.github.hirannor.oms.application.service.customer.error.CustomerNotFound;
import io.github.hirannor.oms.application.service.order.error.OrderCannotBeCreatedWithoutAddress;
import io.github.hirannor.oms.application.service.order.error.OrderCannotBeCreatedWithoutBasketCheckout;
import io.github.hirannor.oms.application.service.order.error.OrderNotFound;
import io.github.hirannor.oms.application.usecase.order.*;
import io.github.hirannor.oms.domain.basket.Basket;
import io.github.hirannor.oms.domain.basket.BasketRepository;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.CustomerRepository;
import io.github.hirannor.oms.domain.inventory.Inventory;
import io.github.hirannor.oms.domain.inventory.InventoryRepository;
import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.OrderRepository;
import io.github.hirannor.oms.domain.order.command.CreateOrder;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApplicationService
class OrderCommandService implements
        OrderCreation,
        OrderCancellation,
        OrderStatusChanging,
        OrderPaymentProcessing {

    private static final Logger LOGGER = LogManager.getLogger(
            OrderCommandService.class
    );

    private final OrderRepository orders;
    private final BasketRepository baskets;
    private final CustomerRepository customers;
    private final Outbox outboxes;
    private final InventoryRepository inventories;

    @Autowired
    OrderCommandService(final OrderRepository orders,
                        final BasketRepository baskets,
                        final CustomerRepository customers,
                        final Outbox outboxes,
                        final InventoryRepository inventories) {
        this.orders = orders;
        this.baskets = baskets;
        this.customers = customers;
        this.outboxes = outboxes;
        this.inventories = inventories;
    }


    @Override
    public Order create(final CreateOrder create) {
        if (create == null) throw new IllegalArgumentException("CreateOrder is null");

        LOGGER.info("Start creating order with orderId={}, customerId={}",
                create.orderId().asText(),
                create.customer().asText());

        final Customer customer = customers.findBy(create.customer())
                .orElseThrow(failBecauseCustomerWasNotFoundBy(create.customer()));

        if (!customer.hasCompleteAddress())
            failBecauseMissingAddressDetails(customer.id());

        final Basket basket = baskets.findBy(create.customer())
                .orElseThrow(failBecauseOfBasketNotFound(create.customer()));

        if (!basket.isCheckedOut())
            throw new OrderCannotBeCreatedWithoutBasketCheckout("Cannot create order, basket is not checked-out");

        final Order order = Order.create(create);
        orders.save(order);

        order.events().forEach(outboxes::save);
        order.clearEvents();

        baskets.deleteBy(order.customer());

        LOGGER.info("Order orderId={} was successfully created for customerId={}",
                order.id().asText(),
                order.id().asText());

        return order;
    }

    @Override
    public void cancelBy(final OrderId id) {
        if (id == null) throw new IllegalArgumentException("OrderId is null");

        LOGGER.info("Start cancellation for orderId={}",
                id.asText()
        );
        final Order order = orders.findBy(id)
                .orElseThrow(failBecauseOrderWasNotFoundBy(id));

        order.cancel();
        orders.save(order);

        order.events().forEach(outboxes::save);
        order.clearEvents();

        LOGGER.info("Order with orderId={} is successfully cancelled", id.asText());
    }

    @Override
    public void change(final ChangeOrderStatus changeStatus) {
        if (changeStatus == null) throw new IllegalArgumentException("ChangeOrderStatus is null");

        LOGGER.info("Changing orderStatus={} for orderId={}",
                changeStatus.status(),
                changeStatus.orderId().asText()
        );

        final Order order = orders.findBy(changeStatus.orderId())
                .orElseThrow(failBecauseOrderWasNotFoundBy(changeStatus.orderId()));

        order.changeStatus(changeStatus.status());
        orders.save(order);

        order.events().forEach(outboxes::save);
        order.clearEvents();

        LOGGER.info("Order status is successfully changed for orderId={}",
                changeStatus.orderId().asText()
        );
    }

    @Override
    public void startProcessing(final StartOrderPaymentProcessing cmd) {
        if (cmd == null) throw new IllegalArgumentException("StartOrderPaymentProcessing is null");

        final Order order = orders.findBy(cmd.orderId())
                .orElseThrow(failBecauseOrderWasNotFoundBy(cmd.orderId()));

        final List<ProductId> productIds = cmd.products()
                .stream()
                .map(ProductQuantity::productId)
                .toList();

        final Map<ProductId, Inventory> inventoryMap = inventories.findAllBy(productIds)
                .stream()
                .collect(Collectors.toMap(Inventory::productId, i -> i));

        for (final ProductQuantity product : cmd.products()) {
            final Inventory inventory = inventoryMap.get(product.productId());

            if (inventory == null)
                throw new IllegalStateException("Inventory not found for productId=" + product.productId().asText());

            inventory.deduct(product.quantity());
            inventories.save(inventory);

            inventory.events().forEach(outboxes::save);
            inventory.clearEvents();
        }

        order.startProcessing();
        orders.save(order);

        order.events().forEach(outboxes::save);
        order.clearEvents();
    }


    private void failBecauseMissingAddressDetails(final CustomerId customer) {
        throw new OrderCannotBeCreatedWithoutAddress("An order cannot be created if the customer: " + customer.asText() + " does not have address details.");
    }

    private Supplier<BasketNotFound> failBecauseOfBasketNotFound(final CustomerId customer) {
        return () -> new BasketNotFound("Basket was not found for customer: " + customer.asText());
    }

    private Supplier<OrderNotFound> failBecauseOrderWasNotFoundBy(OrderId order) {
        return () -> new OrderNotFound("Order not found with id " + order);
    }

    private Supplier<CustomerNotFound> failBecauseCustomerWasNotFoundBy(final CustomerId customer) {
        return () -> new CustomerNotFound("Customer not found with id " + customer);
    }
}

