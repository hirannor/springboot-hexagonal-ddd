package io.github.hirannor.oms.application.port.notification.data;

import io.github.hirannor.oms.application.port.notification.SystemNotificationType;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;

public record OrderPaidNotificationData(
        OrderId orderId,
        String customerName,
        EmailAddress email,
        Money price
) implements NotificationData {
    public static OrderPaidNotificationData from(
            final OrderId orderId,
            final String customerName,
            final EmailAddress email,
            final Money amount) {
        return new OrderPaidNotificationData(orderId, customerName, email, amount);
    }

    @Override
    public SystemNotificationType type() {
        return SystemNotificationType.ORDER_PAID;
    }
}