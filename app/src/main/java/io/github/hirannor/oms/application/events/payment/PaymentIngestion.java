package io.github.hirannor.oms.application.events.payment;

import io.github.hirannor.oms.application.usecase.order.ChangeOrderStatus;
import io.github.hirannor.oms.application.usecase.order.OrderStatusChanging;
import io.github.hirannor.oms.domain.order.OrderStatus;
import io.github.hirannor.oms.domain.payment.events.PaymentCanceled;
import io.github.hirannor.oms.domain.payment.events.PaymentFailed;
import io.github.hirannor.oms.domain.payment.events.PaymentSucceeded;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentIngestion {
    private static final Logger LOGGER = LogManager.getLogger(PaymentIngestion.class);

    private final OrderStatusChanging status;

    @Autowired
    public PaymentIngestion(final OrderStatusChanging status) {
        this.status = status;
    }

    @EventListener
    public void handle(final PaymentSucceeded evt) {
        if (evt == null) throw new IllegalArgumentException("PaymentSucceeded event cannot be null!");

        LOGGER.debug("PaymentSucceeded event received: {}", evt);

        status.change(ChangeOrderStatus.issue(evt.orderId(), OrderStatus.PAID_SUCCESSFULLY));

    }

    @EventListener
    public void handle(final PaymentFailed evt) {
        if (evt == null) throw new IllegalArgumentException("PaymentFailed event cannot be null!");

        LOGGER.debug("PaymentFailed event received: {}", evt);

        status.change(ChangeOrderStatus.issue(evt.orderId(), OrderStatus.PAYMENT_FAILED));

    }

    @EventListener
    public void handle(final PaymentCanceled evt) {
        if (evt == null) throw new IllegalArgumentException("PaymentCanceled event cannot be null!");

        LOGGER.debug("PaymentCanceled event received: {}", evt);
        status.change(ChangeOrderStatus.issue(evt.orderId(), OrderStatus.PAYMENT_CANCELED));

    }
}
