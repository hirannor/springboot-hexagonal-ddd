package io.github.hirannor.oms.application.port.notification.data;

import io.github.hirannor.oms.application.port.notification.SystemNotificationType;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Address;
import io.github.hirannor.oms.domain.order.OrderId;

public record OrderShippedNotificationData(
        OrderId orderId,
        String customerName,
        EmailAddress email,
        Address address
) implements NotificationData {
    public static OrderShippedNotificationData from(
            final OrderId orderId,
            final String customerName,
            final EmailAddress email,
            final Address address) {
        return new OrderShippedNotificationData(orderId, customerName, email, address);
    }

    @Override
    public SystemNotificationType type() {
        return SystemNotificationType.ORDER_SHIPPED;
    }
}