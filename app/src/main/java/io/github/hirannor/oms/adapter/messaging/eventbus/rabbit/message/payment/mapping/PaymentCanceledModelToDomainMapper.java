package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.PaymentCanceledModel;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.domain.payment.events.PaymentCanceled;
import org.springframework.stereotype.Component;

@Component
public class PaymentCanceledModelToDomainMapper implements MessageModelMapper<PaymentCanceledModel, PaymentCanceled> {

    public PaymentCanceledModelToDomainMapper() {
    }

    @Override
    public PaymentCanceled apply(final PaymentCanceledModel model) {
        if (model == null) return null;

        return PaymentCanceled.recreate(
                model.id(),
                PaymentId.from(model.paymentId()),
                OrderId.from(model.orderId())
        );
    }

    @Override
    public Class<PaymentCanceledModel> messageType() {
        return PaymentCanceledModel.class;
    }
}
