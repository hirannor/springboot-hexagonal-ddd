package io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentFailedModel;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.domain.payment.events.PaymentFailed;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.function.Function;

public class PaymentFailedModelToDomainMapper implements Function<PaymentFailedModel, PaymentFailed> {

    public PaymentFailedModelToDomainMapper() {
    }

    @Override
    public PaymentFailed apply(final PaymentFailedModel model) {
        if (model == null) return null;

        return PaymentFailed.recreate(
                MessageId.from(model.eventId()),
                PaymentId.from(model.paymentId()),
                OrderId.from(model.orderId())
        );
    }
}
