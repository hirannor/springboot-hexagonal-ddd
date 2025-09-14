package hu.hirannor.hexagonal.application.usecase.payment;

import hu.hirannor.hexagonal.application.port.payment.PaymentGateway;
import hu.hirannor.hexagonal.application.port.payment.PaymentReceipt;
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
class PaymentCallbackHandlerService implements PaymentCallbackHandling {
    private final OrderRepository orders;
    private final PaymentGateway payment;

    @Autowired
    PaymentCallbackHandlerService(final OrderRepository orders, final PaymentGateway payment) {
        this.orders = orders;
        this.payment = payment;
    }

    @Override
    public void handle(final HandlePaymentCallback command) {
        if (command == null) throw new IllegalArgumentException("command is null");

        final PaymentReceipt receipt = payment.processCallback(command.payload(), command.signatureHeader());

        final Order order = orders.findBy(receipt.orderId())
                .orElseThrow(failBecauseOrderWasNotFoundBy(receipt.orderId()));

        order.handlePaymentResult(receipt);
        orders.save(order);
    }

    private static Supplier<IllegalStateException> failBecauseOrderWasNotFoundBy(final OrderId order) {
        return () -> new IllegalStateException("Order not found by id: " + order.value());
    }
}
