package io.github.hirannor.oms.application.service.notification;

import io.github.hirannor.oms.application.port.notification.NotificationFactory;
import io.github.hirannor.oms.application.port.notification.NotificationMessage;
import io.github.hirannor.oms.application.port.notification.Notificator;
import io.github.hirannor.oms.application.port.notification.data.*;
import io.github.hirannor.oms.application.service.customer.error.CustomerNotFound;
import io.github.hirannor.oms.application.service.order.error.OrderNotFound;
import io.github.hirannor.oms.application.service.product.ProductNotFound;
import io.github.hirannor.oms.application.usecase.notification.NotificationSending;
import io.github.hirannor.oms.application.usecase.notification.SendSystemNotification;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.CustomerRepository;
import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.OrderItem;
import io.github.hirannor.oms.domain.order.OrderRepository;
import io.github.hirannor.oms.domain.product.Product;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.domain.product.ProductRepository;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApplicationService
class NotificationService implements NotificationSending {
    private static final Logger LOGGER = LogManager.getLogger(
            NotificationService.class
    );

    private final OrderRepository orders;
    private final CustomerRepository customers;
    private final Notificator notifications;
    private final NotificationFactory notificationFactory;
    private final ProductRepository products;

    @Autowired
    NotificationService(final OrderRepository orders,
                        final CustomerRepository customers,
                        final Notificator notifications,
                        final NotificationFactory notificationFactory,
                        final ProductRepository products) {
        this.orders = orders;
        this.customers = customers;
        this.notifications = notifications;
        this.notificationFactory = notificationFactory;
        this.products = products;
    }

    @Override
    public void sendBySystem(final SendSystemNotification command) {
        if (command == null) throw new IllegalArgumentException("command is null");

        final Order order = orders.findBy(command.order())
                .orElseThrow(failBecauseOrderWasNotFoundBy(command.order()));

        final Customer customer = customers.findBy(order.customer())
                .orElseThrow(failBecauseCustomerWasNotFoundBy(order.customer()));

        LOGGER.info("Preparing {} notification for customerId={}, email={}",
                command.notificationType(),
                customer.id().asText(),
                customer.emailAddress().value());

        final NotificationData data = switch (command.notificationType()) {
            case ORDER_CREATED -> createOrderCreatedNotificationData(order, customer);
            case ORDER_PAID -> createOrderPaidNotificationData(order, customer);
            case ORDER_SHIPPED -> createOrderShippedNotificationData(order, customer);
        };

        LOGGER.info("Start notificationType={} for customerId={}",
                command.notificationType(),
                customer.id().asText()
        );

        final NotificationMessage message = notificationFactory.createNotification(data);

        notifications.send(message);

        LOGGER.info("NotificationType={} for customerId={} successfully sent",
                command.notificationType(),
                customer.id().asText()
        );
    }

    private OrderCreatedNotificationData createOrderCreatedNotificationData(final Order order, final Customer customer) {
        final List<ProductId> productIds = order.orderItems()
                .stream()
                .map(OrderItem::productId)
                .toList();

        final Map<ProductId, Product> indexedProducts = products.findAllBy(productIds)
                .stream()
                .collect(Collectors.toMap(Product::id, p -> p));

        final List<ProductSummaryData> productSummaries = order.orderItems()
                .stream()
                .map(mapOrderItemToProductSummary(indexedProducts))
                .toList();

        return OrderCreatedNotificationData.from(
                order.id(),
                customer.fullName(),
                customer.emailAddress(),
                productSummaries,
                order.totalPrice()
        );
    }

    private OrderPaidNotificationData createOrderPaidNotificationData(final Order order, final Customer customer) {
        return OrderPaidNotificationData.from(order.id(), customer.fullName(), customer.emailAddress(), order.totalPrice());
    }

    private OrderShippedNotificationData createOrderShippedNotificationData(final Order order, final Customer customer) {
        return OrderShippedNotificationData.from(order.id(), customer.fullName(), customer.emailAddress(), customer.address());
    }

    private Function<OrderItem, ProductSummaryData> mapOrderItemToProductSummary(final Map<ProductId, Product> indexedProducts) {
        return item -> {
            final Product product = indexedProducts.get(item.productId());

            if (product == null)
                throw new ProductNotFound("Product not found for id=" + item.productId().asText());

            return ProductSummaryData.from(item, product);
        };
    }

    private Supplier<CustomerNotFound> failBecauseCustomerWasNotFoundBy(final CustomerId customer) {
        return () -> new CustomerNotFound("Customer not found by " + customer.asText());
    }

    private Supplier<OrderNotFound> failBecauseOrderWasNotFoundBy(final OrderId order) {
        return () -> new OrderNotFound("Order not found by " + order.asText());
    }
}
