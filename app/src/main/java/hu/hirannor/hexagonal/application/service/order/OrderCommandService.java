package hu.hirannor.hexagonal.application.service.order;

import hu.hirannor.hexagonal.application.usecase.order.*;
import hu.hirannor.hexagonal.domain.basket.BasketRepository;
import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.domain.customer.Customer;
import hu.hirannor.hexagonal.domain.customer.CustomerRepository;
import hu.hirannor.hexagonal.domain.order.*;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;
import hu.hirannor.hexagonal.infrastructure.application.ApplicationService;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationService
class OrderCommandService implements
        OrderCreation,
        OrderStatusChanging {

    private static final Logger LOGGER = LogManager.getLogger(
        OrderCommandService.class
    );

    private final OrderRepository orders;
    private final BasketRepository baskets;
    private final CustomerRepository customers;

    @Autowired
    OrderCommandService(final OrderRepository orders,
                        final BasketRepository baskets,
                        final CustomerRepository customers) {
        this.orders = orders;
        this.baskets = baskets;
        this.customers = customers;
    }

    @Override
    public Order create(final CreateOrder create) {
        if (create == null) throw new IllegalArgumentException("CreateOrder is null");

        LOGGER.info("Start creating order with id :{} for customer id: {}",
            create.orderId().asText(),
            create.customer().asText());

        final Customer customer = customers.findBy(create.customer())
            .orElseThrow(failBecauseCustomerWasNotFoundBy(create.customer()));

        if (customer.address() == null)
            failBecauseMissingAddressDetails(customer.id());

        final Order order = Order.create(create);
        orders.save(order);

        baskets.deleteBy(order.customer());

        order.changeStatus(OrderStatus.WAITING_FOR_PAYMENT);
        orders.save(order);

        LOGGER.info("Order with id: {} was successfully created for customer: {}",
            order.id().asText(),
            order.id().asText());

        return order;
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

        LOGGER.info("Order status is successfully changed for order id: {}",
            changeStatus.orderId().asText()
        );
    }


    private void failBecauseMissingAddressDetails(final CustomerId customer) {
        throw new IllegalStateException("Cannot create order for customer: " + customer.asText() + "without address details");
    }

    private Supplier<IllegalStateException> failBecauseOrderWasNotFoundBy(OrderId order) {
        return () -> new IllegalStateException("Order not found with id " + order);
    }

    private Supplier<IllegalStateException> failBecauseCustomerWasNotFoundBy(final CustomerId customer) {
        return () -> new IllegalStateException("Customer not found with id " + customer);
    }

}
