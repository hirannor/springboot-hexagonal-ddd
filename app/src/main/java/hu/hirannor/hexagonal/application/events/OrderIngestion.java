package hu.hirannor.hexagonal.application.events;

import hu.hirannor.hexagonal.application.port.notification.SystemNotificationType;
import hu.hirannor.hexagonal.application.usecase.notification.NotificationSending;
import hu.hirannor.hexagonal.application.usecase.notification.SendSystemNotification;
import hu.hirannor.hexagonal.domain.order.events.OrderCreated;
import hu.hirannor.hexagonal.domain.order.events.OrderPaid;
import hu.hirannor.hexagonal.domain.order.events.OrderShipped;
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

    private final NotificationSending notifications;

    @Autowired
    OrderIngestion(
            final NotificationSending notifications) {
        this.notifications = notifications;
    }


    @TransactionalEventListener
    public void handle(final OrderCreated evt) {
        if (evt == null) throw new IllegalArgumentException("OrderCreated event cannot be null!");

        LOGGER.debug("OrderCreated event received: {}", evt);

        final SendSystemNotification cmd = SendSystemNotification.issue(
                evt.orderId(),
                SystemNotificationType.ORDER_CREATED
        );
        notifications.sendBySystem(cmd);
    }

    @TransactionalEventListener
    public void handle(final OrderPaid evt) {
        if (evt == null) throw new IllegalArgumentException("OrderPaid event cannot be null!");

        LOGGER.debug("OrderPaid event received: {}", evt);

        final SendSystemNotification cmd = SendSystemNotification.issue(
                evt.orderId(),
                SystemNotificationType.ORDER_PAID
        );
        notifications.sendBySystem(cmd);
    }

    @TransactionalEventListener
    public void handle(final OrderShipped evt) {
        if (evt == null) throw new IllegalArgumentException("OrderShipped event cannot be null!");

        LOGGER.debug("OrderShipped event received: {}", evt);

        final SendSystemNotification cmd = SendSystemNotification.issue(
                evt.orderId(),
                SystemNotificationType.ORDER_SHIPPED
        );
        notifications.sendBySystem(cmd);
    }

}
