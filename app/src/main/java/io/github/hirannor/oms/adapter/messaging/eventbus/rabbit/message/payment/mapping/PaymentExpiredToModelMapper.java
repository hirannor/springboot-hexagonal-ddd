package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.PaymentExpiredModel;
import io.github.hirannor.oms.domain.payment.events.PaymentExpired;
import org.springframework.stereotype.Component;

@Component
public class PaymentExpiredToModelMapper implements DomainEventMapper<PaymentExpired, PaymentExpiredModel> {

    public PaymentExpiredToModelMapper() {
    }

    @Override
    public PaymentExpiredModel apply(final PaymentExpired evt) {
        if (evt == null) return null;

        return new PaymentExpiredModel(evt.id().asText(),
                evt.paymentId().asText(),
                evt.orderId().asText());
    }

    @Override
    public Class<PaymentExpired> eventType() {
        return PaymentExpired.class;
    }
}
