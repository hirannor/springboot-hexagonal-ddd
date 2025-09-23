package io.github.hirannor.oms.application.port.notification.data;

import io.github.hirannor.oms.application.port.notification.SystemNotificationType;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.order.OrderId;

public sealed interface NotificationData permits
        OrderCreatedNotificationData,
        OrderPaidNotificationData,
        OrderShippedNotificationData {
    SystemNotificationType type();

    OrderId orderId();

    String customerName();

    EmailAddress email();
}

