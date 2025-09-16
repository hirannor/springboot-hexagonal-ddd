package hu.hirannor.hexagonal.application.service.order;

import hu.hirannor.hexagonal.application.usecase.order.ChangeOrderStatus;
import hu.hirannor.hexagonal.application.usecase.order.OrderCreation;
import hu.hirannor.hexagonal.application.usecase.order.OrderStatusChanging;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderRepository;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;


@Service
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ
)
class OrderCommandService implements
        OrderCreation,
        OrderStatusChanging {

    private static final Logger LOGGER = LogManager.getLogger(
        OrderCommandService.class
    );

    private final OrderRepository orders;


    @Autowired
    OrderCommandService(final OrderRepository orders) {
        this.orders = orders;
    }

    @Override
    public Order create(final CreateOrder create) {
        if (create == null) throw new IllegalArgumentException("CreateOrder is null");

        LOGGER.info("Start creating order with id :{} for customer id: {}",
            create.orderId().asText(),
            create.customer().asText());

        final Order order = Order.create(create);
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

    private Supplier<IllegalStateException> failBecauseOrderWasNotFoundBy(OrderId order) {
        return () -> new IllegalStateException("Order not found with id " + order);
    }

}
