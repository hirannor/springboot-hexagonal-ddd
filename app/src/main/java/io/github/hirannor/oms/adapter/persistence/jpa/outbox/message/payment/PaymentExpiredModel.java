package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment;


import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModel;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record PaymentExpiredModel(
        MessageId id,
        String paymentId,
        String orderId
) implements MessageModel {
}