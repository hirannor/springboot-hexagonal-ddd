package io.github.hirannor.oms.application.service.notification;

import io.github.hirannor.oms.application.port.notification.NotificationData;
import io.github.hirannor.oms.application.port.notification.NotificationFactory;
import io.github.hirannor.oms.application.port.notification.NotificationMessage;
import io.github.hirannor.oms.application.port.notification.Notificator;
import io.github.hirannor.oms.application.service.customer.error.CustomerNotFound;
import io.github.hirannor.oms.application.service.order.error.OrderNotFound;
import io.github.hirannor.oms.application.usecase.notification.NotificationSending;
import io.github.hirannor.oms.application.usecase.notification.SendSystemNotification;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.CustomerRepository;
import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.OrderRepository;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Supplier;

@ApplicationService
class NotificationService implements NotificationSending {
    private static final Logger LOGGER = LogManager.getLogger(
        NotificationService.class
    );
    private final OrderRepository orders;
    private final CustomerRepository customers;
    private final Notificator notifications;
    private final NotificationFactory notificationFactory;

    @Autowired
    NotificationService(final OrderRepository orders,
                               final CustomerRepository customers,
                               final Notificator notifications,
                               final NotificationFactory notificationFactory) {
        this.orders = orders;
        this.customers = customers;
        this.notifications = notifications;
        this.notificationFactory = notificationFactory;
    }

    @Override
    public void sendBySystem(final SendSystemNotification command) {
        if (command == null) throw new IllegalArgumentException("command is null");

        final Order order = orders.findBy(command.order())
                .orElseThrow(failBecauseOrderWasNotFoundBy(command.order()));

        final Customer customer = customers.findBy(order.customer())
                .orElseThrow(failBecauseCustomerWasNotFoundBy(order.customer()));

        final NotificationMessage msg = notificationFactory.createNotification(
                new NotificationData(
                        command.notificationType(),
                        order.id(),
                        customer.fullName(),
                        customer.emailAddress(),
                        customer.address()
        ));

        LOGGER.info("Start notification type: {} for customer: {}",
            command.notificationType(),
            customer.id().asText()
        );

        notifications.send(msg);

        LOGGER.info("Notification: {} for customer: {} successfully sent",
            command.notificationType(),
            customer.id().asText()
        );
    }

    private Supplier<CustomerNotFound> failBecauseCustomerWasNotFoundBy(final CustomerId customer) {
        return () -> new CustomerNotFound("Customer not found by " + customer.asText());
    }

    private Supplier<OrderNotFound> failBecauseOrderWasNotFoundBy(final OrderId order) {
        return () -> new OrderNotFound("Order not found by " + order.asText());
    }
}
