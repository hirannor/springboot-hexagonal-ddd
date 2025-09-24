package io.github.hirannor.oms.application.port.notification.data;

import io.github.hirannor.oms.application.port.notification.SystemNotificationType;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;

import java.util.List;

public record OrderCreatedNotificationData(
        OrderId orderId,
        String customerName,
        EmailAddress email,
        List<ProductSummaryData> productSummaries,
        Money totalPrice
) implements NotificationData {

    public static OrderCreatedNotificationData from(
            final OrderId orderId,
            final String customerName,
            final EmailAddress email,
            final List<ProductSummaryData> productSummaries,
            final Money totalPrice) {
        return new OrderCreatedNotificationData(orderId, customerName, email, productSummaries, totalPrice);
    }

    @Override
    public SystemNotificationType type() {
        return SystemNotificationType.ORDER_CREATED;
    }
}