package io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentCanceledModel;
import io.github.hirannor.oms.domain.payment.events.PaymentCanceled;

import java.util.function.Function;

public class PaymentCanceledToModelMapper implements Function<PaymentCanceled, PaymentCanceledModel> {

    public PaymentCanceledToModelMapper() {
    }

    @Override
    public PaymentCanceledModel apply(final PaymentCanceled evt) {
        if (evt == null) return null;

        return new PaymentCanceledModel(evt.id().asText(),
                evt.paymentId().asText(),
                evt.orderId().asText());
    }
}
