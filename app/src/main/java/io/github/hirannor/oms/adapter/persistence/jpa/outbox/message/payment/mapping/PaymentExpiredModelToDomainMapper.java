package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment.PaymentExpiredModel;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.domain.payment.events.PaymentExpired;
import org.springframework.stereotype.Component;

@Component(value = "PaymentExpiredModelToDomainMapper")
public class PaymentExpiredModelToDomainMapper implements MessageModelMapper<PaymentExpiredModel, PaymentExpired> {

    public PaymentExpiredModelToDomainMapper() {
    }

    @Override
    public PaymentExpired apply(final PaymentExpiredModel model) {
        if (model == null) return null;

        return PaymentExpired.recreate(
                model.id(),
                PaymentId.from(model.paymentId()),
                OrderId.from(model.orderId())
        );
    }

    @Override
    public Class<PaymentExpiredModel> messageType() {
        return PaymentExpiredModel.class;
    }
}
