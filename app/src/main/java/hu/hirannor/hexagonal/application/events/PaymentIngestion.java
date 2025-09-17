package hu.hirannor.hexagonal.application.events;

import hu.hirannor.hexagonal.domain.payment.events.PaymentCanceled;
import hu.hirannor.hexagonal.domain.payment.events.PaymentFailed;
import hu.hirannor.hexagonal.domain.payment.events.PaymentInitialized;
import hu.hirannor.hexagonal.domain.payment.events.PaymentSucceeded;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PaymentIngestion {
    private static final Logger LOGGER = LogManager.getLogger(PaymentIngestion.class);


    public PaymentIngestion() {
    }

    @TransactionalEventListener
    public void handle(final PaymentSucceeded evt) {
        if (evt == null) throw new IllegalArgumentException("PaymentSucceeded event cannot be null!");

        LOGGER.debug("PaymentSucceeded event received: {}", evt);

    }

    @TransactionalEventListener
    public void handle(final PaymentInitialized evt) {
        if (evt == null) throw new IllegalArgumentException("PaymentInitialized event cannot be null!");

        LOGGER.debug("PaymentInitialized event received: {}", evt);
    }

    @TransactionalEventListener
    public void handle(final PaymentFailed evt) {
        if (evt == null) throw new IllegalArgumentException("PaymentFailed event cannot be null!");

        LOGGER.debug("PaymentFailed event received: {}", evt);
    }

    @TransactionalEventListener
    public void handle(final PaymentCanceled evt) {
        if (evt == null) throw new IllegalArgumentException("PaymentCanceled event cannot be null!");

        LOGGER.debug("PaymentCanceled event received: {}", evt);
    }
}
