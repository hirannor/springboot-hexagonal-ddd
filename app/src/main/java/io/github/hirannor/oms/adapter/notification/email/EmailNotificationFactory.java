package io.github.hirannor.oms.adapter.notification.email;

import io.github.hirannor.oms.application.port.notification.EmailNotificationMessage;
import io.github.hirannor.oms.application.port.notification.NotificationData;
import io.github.hirannor.oms.application.port.notification.NotificationFactory;
import io.github.hirannor.oms.application.port.notification.NotificationMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
class EmailNotificationFactory implements NotificationFactory {

    private static final String ORDER_CREATED_TEMPLATE = "notifications/order-created";
    private static final String ORDER_PAID_TEMPLATE = "notifications/order-paid";
    private static final String ORDER_SHIPPED_TEMPLATE = "notifications/order-shipped";
    private static final String VAR_CUSTOMER_NAME = "customerName";
    private static final String VAR_ORDER_ID = "orderId";
    private static final String VAR_COUNTRY = "country";
    private static final String VAR_CITY = "city";
    private static final String VAR_POSTAL_CODE = "postalCode";
    private static final String VAR_STREET = "streetAddress";

    EmailNotificationFactory() {
    }

    @Override
    public NotificationMessage createNotification(final NotificationData data) {
        return switch (data.type()) {
            case ORDER_CREATED -> createOrderCreatedNotificationFrom(data);
            case ORDER_PAID -> createOrderPaidNotificationFrom(data);
            case ORDER_SHIPPED -> createOrderShippedNotificationFrom(data);
        };
    }

    private EmailNotificationMessage createOrderShippedNotificationFrom(final NotificationData data) {
        return EmailNotificationMessage.create(
                data.email().value(),
                "Your order has shipped!",
                ORDER_SHIPPED_TEMPLATE,
                Map.of(
                        VAR_CUSTOMER_NAME, data.customerName(),
                        VAR_ORDER_ID, data.orderId().value(),
                        VAR_COUNTRY, data.address().country().name(),
                        VAR_CITY, data.address().city(),
                        VAR_POSTAL_CODE, data.address().postalCode().value(),
                        VAR_STREET, data.address().streetAddress()
                )
        );
    }

    private EmailNotificationMessage createOrderPaidNotificationFrom(final NotificationData data) {
        return EmailNotificationMessage.create(
                data.email().value(),
                "Payment received",
                ORDER_PAID_TEMPLATE,
                Map.of(
                        VAR_CUSTOMER_NAME, data.customerName(),
                        VAR_ORDER_ID, data.orderId().value()
                )
        );
    }

    private EmailNotificationMessage createOrderCreatedNotificationFrom(final NotificationData data) {
        return EmailNotificationMessage.create(
                data.email().value(),
                "Your order confirmation",
                ORDER_CREATED_TEMPLATE,
                Map.of(
                        VAR_CUSTOMER_NAME, data.customerName(),
                        VAR_ORDER_ID, data.orderId().value()
                )
        );
    }
}
