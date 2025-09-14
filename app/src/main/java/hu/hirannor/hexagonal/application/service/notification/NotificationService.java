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
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final Notificator notificator;
    private final NotificationFactory notificationFactory;

    @Autowired
    NotificationService(final OrderRepository orderRepository,
                               final CustomerRepository customerRepository,
                               final Notificator notificator,
                               final NotificationFactory notificationFactory) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.notificator = notificator;
        this.notificationFactory = notificationFactory;
    }

    @Override
    public void sentBySystem(final SendSystemNotification command) {
        if (command == null) throw new IllegalArgumentException("command is null");

        final Order order = orderRepository.findBy(command.order())
                .orElseThrow(failBecauseOrderWasNotFoundBy(command.order()));

        final Customer customer = customerRepository.findBy(order.customer())
                .orElseThrow(failBecauseCustomerWasNotFoundBy(order.customer()));

        final NotificationMessage msg = notificationFactory.createNotification(
                new NotificationData(
                        command.notificationType(),
                        order.id(),
                        customer.fullName(),
                        customer.emailAddress(),
                        customer.address()
        ));

        notificator.send(msg);
    }

    private static Supplier<IllegalStateException> failBecauseCustomerWasNotFoundBy(final CustomerId customer) {
        return () -> new IllegalStateException("Customer not found by " + customer.asText());
    }

    private static Supplier<IllegalStateException> failBecauseOrderWasNotFoundBy(final OrderId order) {
        return () -> new IllegalStateException("Order not found by " + order.asText());
    }
}
