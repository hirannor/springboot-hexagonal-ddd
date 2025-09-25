package io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentExpiredModel;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.domain.payment.events.PaymentExpired;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;
import org.springframework.stereotype.Component;

@Component(value = "PaymentExpiredModelToDomainMapper")
public class PaymentExpiredModelToDomainMapper implements DomainEventModelMapper<PaymentExpiredModel, PaymentExpired> {

    public PaymentExpiredModelToDomainMapper() {
    }

    @Override
    public PaymentExpired apply(final PaymentExpiredModel model) {
        if (model == null) return null;

        return PaymentExpired.recreate(
                MessageId.from(model.eventId()),
                PaymentId.from(model.paymentId()),
                OrderId.from(model.orderId())
        );
    }

    @Override
    public Class<PaymentExpiredModel> eventType() {
        return PaymentExpiredModel.class;
    }
}
