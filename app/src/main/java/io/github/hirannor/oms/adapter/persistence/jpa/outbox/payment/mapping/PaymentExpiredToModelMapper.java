package io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentExpiredModel;
import io.github.hirannor.oms.domain.payment.events.PaymentExpired;

import java.util.function.Function;

public class PaymentExpiredToModelMapper implements Function<PaymentExpired, PaymentExpiredModel> {

    public PaymentExpiredToModelMapper() {
    }

    @Override
    public PaymentExpiredModel apply(final PaymentExpired evt) {
        if (evt == null) return null;

        return new PaymentExpiredModel(evt.id().asText(),
                evt.paymentId().asText(),
                evt.orderId().asText());
    }
}
