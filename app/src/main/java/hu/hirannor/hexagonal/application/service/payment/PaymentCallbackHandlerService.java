package hu.hirannor.hexagonal.application.service.payment;

import hu.hirannor.hexagonal.application.port.payment.PaymentGateway;
import hu.hirannor.hexagonal.application.usecase.payment.HandlePaymentCallback;
import hu.hirannor.hexagonal.application.usecase.payment.PaymentCallbackHandling;
import hu.hirannor.hexagonal.domain.order.*;
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
class PaymentCallbackHandlerService implements PaymentCallbackHandling {

    private static final Logger LOGGER = LogManager.getLogger(
        PaymentCallbackHandlerService.class
    );

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

        LOGGER.info("Start processing payment callback...");

        payment.processCallback(command.payload(), command.signatureHeader())
            .ifPresentOrElse(paymentReceipt -> {
                LOGGER.info("Payment was: {} for order id: {}",
                    paymentReceipt.status(),
                    paymentReceipt.orderId().asText()
                );

                final Order toPersist = orders.findBy(paymentReceipt.orderId())
                    .orElseThrow(failBecauseOrderWasNotFoundBy(paymentReceipt.orderId()))
                    .handlePaymentResult(paymentReceipt);

                orders.save(toPersist);
            },
                () -> LOGGER.warn("Skipping payment callback handling")
            );

        LOGGER.info("Processing payment callback was successful...");

    }

    private static Supplier<IllegalStateException> failBecauseOrderWasNotFoundBy(final OrderId order) {
        return () -> new IllegalStateException("Order not found by id: " + order.value());
    }
}
