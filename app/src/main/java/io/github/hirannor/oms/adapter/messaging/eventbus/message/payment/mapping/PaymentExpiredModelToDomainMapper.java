package io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentExpiredModel;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.domain.payment.events.PaymentExpired;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.function.Function;

public class PaymentExpiredModelToDomainMapper implements Function<PaymentExpiredModel, PaymentExpired> {

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
}
