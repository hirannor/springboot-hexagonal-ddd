package hu.hirannor.hexagonal.application.events;

import hu.hirannor.hexagonal.application.usecase.basket.BasketDeletion;
import hu.hirannor.hexagonal.application.usecase.order.ChangeOrderStatus;
import hu.hirannor.hexagonal.application.usecase.order.OrderStatusChanging;
import hu.hirannor.hexagonal.domain.order.OrderStatus;
import hu.hirannor.hexagonal.domain.order.events.OrderCreated;
import hu.hirannor.hexagonal.domain.order.events.OrderPaid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderIngestion {
    private static final Logger LOGGER = LogManager.getLogger(
        OrderIngestion.class
    );

    private final OrderStatusChanging orderStatusChanging;
    private final BasketDeletion basketDeletion;

    @Autowired
    OrderIngestion(
            final OrderStatusChanging orderStatusChanging,
            final BasketDeletion basketDeletion) {
        this.orderStatusChanging = orderStatusChanging;
        this.basketDeletion = basketDeletion;
    }


    @TransactionalEventListener
    public void handle(final OrderCreated evt) {
        if (evt == null) throw new IllegalArgumentException("OrderCreated event cannot be null!");

        LOGGER.debug("OrderCreated event received: {}", evt);

        orderStatusChanging.change(
            ChangeOrderStatus.issue(
                evt.orderId(),
                OrderStatus.WAITING_FOR_PAYMENT
            )
        );

        basketDeletion.deleteBy(evt.customerId());
    }

    @TransactionalEventListener
    public void handle(final OrderPaid evt) {
        if (evt == null) throw new IllegalArgumentException("OrderPaid event cannot be null!");

        LOGGER.debug("OrderPaid event received: {}", evt);

        orderStatusChanging.change(
                ChangeOrderStatus.issue(
                        evt.orderId(),
                        OrderStatus.PROCESSING
                )
        );
    }

}
