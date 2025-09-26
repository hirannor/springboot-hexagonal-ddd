package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment.PaymentFailedModel;
import io.github.hirannor.oms.domain.payment.events.PaymentFailed;
import org.springframework.stereotype.Component;

@Component(value = "PaymentFailedToModelMapper")
public class PaymentFailedToModelMapper implements MessageMapper<PaymentFailed, PaymentFailedModel> {

    public PaymentFailedToModelMapper() {
    }

    @Override
    public PaymentFailedModel apply(final PaymentFailed evt) {
        if (evt == null) return null;

        return new PaymentFailedModel(evt.id(),
                evt.paymentId().asText(),
                evt.orderId().asText());
    }

    @Override
    public Class<PaymentFailed> messageType() {
        return PaymentFailed.class;
    }
}
