package hu.hirannor.hexagonal.application.service.order;

import hu.hirannor.hexagonal.application.usecase.order.ChangeOrderStatus;
import hu.hirannor.hexagonal.application.usecase.order.OrderCancellation;
import hu.hirannor.hexagonal.application.usecase.order.OrderCreation;
import hu.hirannor.hexagonal.application.usecase.order.OrderStatusChanging;
import hu.hirannor.hexagonal.domain.basket.BasketRepository;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderRepository;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;
import hu.hirannor.hexagonal.infrastructure.application.ApplicationService;
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


    @Autowired
    OrderCommandService(final OrderRepository orders, final BasketRepository baskets) {
        this.orders = orders;
        this.baskets = baskets;
    }

    @Override
    public Order create(final CreateOrder create) {
        if (create == null) throw new IllegalArgumentException("CreateOrder is null");

        LOGGER.info("Start creating order with id :{} for customer id: {}",
            create.orderId().asText(),
            create.customer().asText());

        final Order order = Order.create(create);
        orders.save(order);

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

        LOGGER.info("Order status is successfully changed for order id: {}",
            changeStatus.orderId().asText()
        );
    }

    private Supplier<IllegalStateException> failBecauseOrderWasNotFoundBy(OrderId order) {
        return () -> new IllegalStateException("Order not found with id " + order);
    }

}
