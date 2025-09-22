package io.github.hirannor.oms.application.events.order;


import io.github.hirannor.oms.application.port.notification.SystemNotificationType;
import io.github.hirannor.oms.application.usecase.notification.NotificationSending;
import io.github.hirannor.oms.application.usecase.notification.SendSystemNotification;
import io.github.hirannor.oms.domain.order.events.OrderCreated;
import io.github.hirannor.oms.domain.order.events.OrderPaid;
import io.github.hirannor.oms.domain.order.events.OrderShipped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationIngestion {
    private static final Logger LOGGER = LogManager.getLogger(
            OrderNotificationIngestion.class
    );

    private final NotificationSending notifications;

    @Autowired
    OrderNotificationIngestion(final NotificationSending notifications) {
        this.notifications = notifications;
    }


    @Async
    @EventListener
    public void handle(final OrderCreated evt) {
        if (evt == null) throw new IllegalArgumentException("OrderCreated event cannot be null!");

        LOGGER.debug("OrderCreated event received: {}", evt);

        final SendSystemNotification cmd = SendSystemNotification.issue(
                evt.orderId(),
                SystemNotificationType.ORDER_CREATED
        );
        notifications.sendBySystem(cmd);
    }

    @Async
    @EventListener
    public void handle(final OrderPaid evt) {
        if (evt == null) throw new IllegalArgumentException("OrderPaid event cannot be null!");

        LOGGER.debug("OrderPaid event received: {}", evt);

        final SendSystemNotification cmd = SendSystemNotification.issue(
                evt.orderId(),
                SystemNotificationType.ORDER_PAID
        );
        notifications.sendBySystem(cmd);
    }

    @Async
    @EventListener
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
