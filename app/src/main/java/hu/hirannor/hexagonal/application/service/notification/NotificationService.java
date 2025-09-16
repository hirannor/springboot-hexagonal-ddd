package hu.hirannor.hexagonal.application.service.notification;

import hu.hirannor.hexagonal.application.port.notification.NotificationData;
import hu.hirannor.hexagonal.application.port.notification.NotificationFactory;
import hu.hirannor.hexagonal.application.port.notification.NotificationMessage;
import hu.hirannor.hexagonal.application.port.notification.Notificator;
import hu.hirannor.hexagonal.application.usecase.notification.NotificationSending;
import hu.hirannor.hexagonal.application.usecase.notification.SendSystemNotification;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.customer.Customer;
import hu.hirannor.hexagonal.domain.customer.CustomerRepository;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ
)
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

    private Supplier<IllegalStateException> failBecauseCustomerWasNotFoundBy(final CustomerId customer) {
        return () -> new IllegalStateException("Customer not found by " + customer.asText());
    }

    private Supplier<IllegalStateException> failBecauseOrderWasNotFoundBy(final OrderId order) {
        return () -> new IllegalStateException("Order not found by " + order.asText());
    }
}
