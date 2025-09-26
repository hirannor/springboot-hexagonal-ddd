package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.PaymentExpiredModel;
import io.github.hirannor.oms.domain.payment.events.PaymentExpired;
import org.springframework.stereotype.Component;

@Component
public class PaymentExpiredToModelMapper implements MessageMapper<PaymentExpired, PaymentExpiredModel> {

    public PaymentExpiredToModelMapper() {
    }

    @Override
    public PaymentExpiredModel apply(final PaymentExpired evt) {
        if (evt == null) return null;

        return new PaymentExpiredModel(evt.id(),
                evt.paymentId().asText(),
                evt.orderId().asText());
    }

    @Override
    public Class<PaymentExpired> messageType() {
        return PaymentExpired.class;
    }
}
