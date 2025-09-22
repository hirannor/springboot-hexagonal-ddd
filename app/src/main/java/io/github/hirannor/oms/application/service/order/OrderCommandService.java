package io.github.hirannor.oms.application.service.order;

import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.application.service.customer.error.CustomerNotFound;
import io.github.hirannor.oms.application.service.order.error.OrderCannotBeCreatedWithoutAddress;
import io.github.hirannor.oms.application.service.order.error.OrderNotFound;
import io.github.hirannor.oms.application.usecase.order.ChangeOrderStatus;
import io.github.hirannor.oms.application.usecase.order.OrderCancellation;
import io.github.hirannor.oms.application.usecase.order.OrderCreation;
import io.github.hirannor.oms.application.usecase.order.OrderStatusChanging;
import io.github.hirannor.oms.domain.basket.BasketRepository;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.CustomerRepository;
import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.OrderRepository;
import io.github.hirannor.oms.domain.order.command.CreateOrder;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import io.github.hirannor.oms.infrastructure.messaging.MessagePublisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Supplier;

@ApplicationService
class OrderCommandService implements
        OrderCreation,
        OrderCancellation,
        OrderStatusChanging {

    private static final Logger LOGGER = LogManager.getLogger(
        OrderCommandService.class
    );

    private final OrderRepository orders;
    private final BasketRepository baskets;
    private final CustomerRepository customers;
    private final Outbox outboxes;

    @Autowired
    OrderCommandService(final OrderRepository orders,
                        final BasketRepository baskets,
                        final CustomerRepository customers,
                        final Outbox outboxes) {
        this.orders = orders;
        this.baskets = baskets;
        this.customers = customers;
        this.outboxes = outboxes;
    }

    @Override
    public Order create(final CreateOrder create) {
        if (create == null) throw new IllegalArgumentException("CreateOrder is null");

        LOGGER.info("Start creating order with id :{} for customer id: {}",
            create.orderId().asText(),
            create.customer().asText());

        final Customer customer = customers.findBy(create.customer())
            .orElseThrow(failBecauseCustomerWasNotFoundBy(create.customer()));

        if (!customer.address().isComplete())
            failBecauseMissingAddressDetails(customer.id());

        final Order order = Order.create(create);
        orders.save(order);

        order.events().forEach(outboxes::save);
        order.clearEvents();

        baskets.deleteBy(order.customer());

        LOGGER.info("Order with id: {} was successfully created for customer: {}",
            order.id().asText(),
            order.id().asText());

        return order;
    }

    @Override
    public void cancelBy(final OrderId id) {
        if (id == null) throw new IllegalArgumentException("OrderId is null");

        LOGGER.info("Start cancellation for id id: {}",
            id.asText()
        );
        final Order order = orders.findBy(id)
                .orElseThrow(failBecauseOrderWasNotFoundBy(id));

        order.cancel();
        orders.save(order);

        order.events().forEach(outboxes::save);
        order.clearEvents();

        LOGGER.info("Order with id: {} is successfully cancelled", id.asText());
    }

    @Override
    public void change(final ChangeOrderStatus changeStatus) {
        if (changeStatus == null) throw new IllegalArgumentException("ChangeOrderStatus is null");

        LOGGER.info("Changing order status to: {} for order id: {}",
            changeStatus.status(),
            changeStatus.orderId().asText()
        );

        final Order order = orders.findBy(changeStatus.orderId())
                .orElseThrow(failBecauseOrderWasNotFoundBy(changeStatus.orderId()));

        order.changeStatus(changeStatus.status());
        orders.save(order);

        order.events().forEach(outboxes::save);
        order.clearEvents();

        LOGGER.info("Order status is successfully changed for order id: {}",
            changeStatus.orderId().asText()
        );
    }


    private void failBecauseMissingAddressDetails(final CustomerId customer) {
        throw new OrderCannotBeCreatedWithoutAddress("An order cannot be created if the customer: " + customer.asText() + " does not have address details.");
    }

    private Supplier<OrderNotFound> failBecauseOrderWasNotFoundBy(OrderId order) {
        return () -> new OrderNotFound("Order not found with id " + order);
    }

    private Supplier<CustomerNotFound> failBecauseCustomerWasNotFoundBy(final CustomerId customer) {
        return () -> new CustomerNotFound("Customer not found with id " + customer);
    }

}
