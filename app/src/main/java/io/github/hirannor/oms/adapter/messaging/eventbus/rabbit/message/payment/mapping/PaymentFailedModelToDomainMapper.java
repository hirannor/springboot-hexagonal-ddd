package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.PaymentFailedModel;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.domain.payment.events.PaymentFailed;
import org.springframework.stereotype.Component;

@Component
public class PaymentFailedModelToDomainMapper implements MessageModelMapper<PaymentFailedModel, PaymentFailed> {

    public PaymentFailedModelToDomainMapper() {
    }

    @Override
    public PaymentFailed apply(final PaymentFailedModel model) {
        if (model == null) return null;

        return PaymentFailed.recreate(
                model.id(),
                PaymentId.from(model.paymentId()),
                OrderId.from(model.orderId())
        );
    }

    @Override
    public Class<PaymentFailedModel> messageType() {
        return PaymentFailedModel.class;
    }
}
