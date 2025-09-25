package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.PaymentCanceledModel;
import io.github.hirannor.oms.domain.payment.events.PaymentCanceled;
import org.springframework.stereotype.Component;

@Component
public class PaymentCanceledToModelMapper implements DomainEventMapper<PaymentCanceled, PaymentCanceledModel> {

    public PaymentCanceledToModelMapper() {
    }

    @Override
    public PaymentCanceledModel apply(final PaymentCanceled evt) {
        if (evt == null) return null;

        return new PaymentCanceledModel(evt.id().asText(),
                evt.paymentId().asText(),
                evt.orderId().asText());
    }

    @Override
    public Class<PaymentCanceled> eventType() {
        return PaymentCanceled.class;
    }
}
