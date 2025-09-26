package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment.PaymentCanceledModel;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.domain.payment.events.PaymentCanceled;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;
import org.springframework.stereotype.Component;

@Component(value = "PaymentCanceledModelToDomainMapper")
public class PaymentCanceledModelToDomainMapper implements MessageModelMapper<PaymentCanceledModel, PaymentCanceled> {

    public PaymentCanceledModelToDomainMapper() {
    }

    @Override
    public PaymentCanceled apply(final PaymentCanceledModel model) {
        if (model == null) return null;

        return PaymentCanceled.recreate(
                MessageId.from(model.id()),
                PaymentId.from(model.paymentId()),
                OrderId.from(model.orderId())
        );
    }

    @Override
    public Class<PaymentCanceledModel> eventType() {
        return PaymentCanceledModel.class;
    }
}
