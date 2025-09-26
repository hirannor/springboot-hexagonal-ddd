package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment.PaymentCanceledModel;
import io.github.hirannor.oms.domain.payment.events.PaymentCanceled;
import org.springframework.stereotype.Component;

@Component(value = "PaymentCanceledToModelMapper")
public class PaymentCanceledToModelMapper implements MessageMapper<PaymentCanceled, PaymentCanceledModel> {

    public PaymentCanceledToModelMapper() {
    }

    @Override
    public PaymentCanceledModel apply(final PaymentCanceled evt) {
        if (evt == null) return null;

        return new PaymentCanceledModel(evt.id(),
                evt.paymentId().asText(),
                evt.orderId().asText());
    }

    @Override
    public Class<PaymentCanceled> messageType() {
        return PaymentCanceled.class;
    }
}
