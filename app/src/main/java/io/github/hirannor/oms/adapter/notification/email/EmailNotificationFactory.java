package io.github.hirannor.oms.adapter.notification.email;

import io.github.hirannor.oms.application.port.notification.EmailNotificationMessage;
import io.github.hirannor.oms.application.port.notification.NotificationFactory;
import io.github.hirannor.oms.application.port.notification.NotificationMessage;
import io.github.hirannor.oms.application.port.notification.data.NotificationData;
import io.github.hirannor.oms.application.port.notification.data.OrderCreatedNotificationData;
import io.github.hirannor.oms.application.port.notification.data.OrderPaidNotificationData;
import io.github.hirannor.oms.application.port.notification.data.OrderShippedNotificationData;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
class EmailNotificationFactory implements NotificationFactory {

    private static final String ORDER_CREATED_TEMPLATE = "notifications/order-created";
    private static final String ORDER_PAID_TEMPLATE = "notifications/order-paid";
    private static final String ORDER_SHIPPED_TEMPLATE = "notifications/order-shipped";

    private static final String VAR_CUSTOMER_NAME = "customerName";
    private static final String VAR_ORDER_ID = "orderId";
    private static final String VAR_ADDRESS = "address";
    private static final String VAR_PRODUCTS = "products";
    private static final String VAR_TOTAL_PRICE = "totalPrice";

    EmailNotificationFactory() {
    }

    @Override
    public NotificationMessage createNotification(final NotificationData base) {
        return switch (base) {
            case OrderCreatedNotificationData data -> createOrderCreatedNotificationFrom(data);
            case OrderPaidNotificationData data -> createOrderPaidNotificationFrom(data);
            case OrderShippedNotificationData data -> createOrderShippedNotificationFrom(data);
        };
    }

    private EmailNotificationMessage createOrderShippedNotificationFrom(final OrderShippedNotificationData data) {
        return EmailNotificationMessage.create(
                data.email().value(),
                "Your order has shipped!",
                ORDER_SHIPPED_TEMPLATE,
                Map.of(
                        VAR_CUSTOMER_NAME, data.customerName(),
                        VAR_ORDER_ID, data.orderId().value(),
                        VAR_ADDRESS, data.address()
                )
        );
    }

    private EmailNotificationMessage createOrderPaidNotificationFrom(final OrderPaidNotificationData data) {
        return EmailNotificationMessage.create(
                data.email().value(),
                "Payment received",
                ORDER_PAID_TEMPLATE,
                Map.of(
                        VAR_CUSTOMER_NAME, data.customerName(),
                        VAR_ORDER_ID, data.orderId().value(),
                        VAR_TOTAL_PRICE, data.price()
                )
        );
    }

    private EmailNotificationMessage createOrderCreatedNotificationFrom(final OrderCreatedNotificationData data) {
        return EmailNotificationMessage.create(
                data.email().value(),
                "Your order confirmation",
                ORDER_CREATED_TEMPLATE,
                Map.of(
                        VAR_CUSTOMER_NAME, data.customerName(),
                        VAR_ORDER_ID, data.orderId().value(),
                        VAR_PRODUCTS, data.productSummaries(),
                        VAR_TOTAL_PRICE, data.totalPrice()
                )
        );
    }
}
