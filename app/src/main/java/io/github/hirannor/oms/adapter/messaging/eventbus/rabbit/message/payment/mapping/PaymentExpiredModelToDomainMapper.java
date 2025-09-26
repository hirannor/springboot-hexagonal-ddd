package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.PaymentExpiredModel;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.domain.payment.events.PaymentExpired;
import org.springframework.stereotype.Component;

@Component
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
