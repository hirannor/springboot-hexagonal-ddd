package io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentCanceledModel;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.domain.payment.events.PaymentCanceled;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.function.Function;

public class PaymentCanceledModelToDomainMapper implements Function<PaymentCanceledModel, PaymentCanceled> {

    public PaymentCanceledModelToDomainMapper() {
    }

    @Override
    public PaymentCanceled apply(final PaymentCanceledModel model) {
        if (model == null) return null;

        return PaymentCanceled.recreate(
                MessageId.from(model.eventId()),
                PaymentId.from(model.paymentId()),
                OrderId.from(model.orderId())
        );
    }
}
