package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModel;

public record PaymentFailedModel(
        String id,
        String paymentId,
        String orderId
) implements MessageModel {
}