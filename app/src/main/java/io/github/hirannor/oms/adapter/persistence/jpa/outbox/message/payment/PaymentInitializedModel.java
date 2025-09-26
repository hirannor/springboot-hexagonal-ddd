package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModel;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.math.BigDecimal;

public record PaymentInitializedModel(
        MessageId id,
        String paymentId,
        String orderId,
        BigDecimal amount,
        String currency
) implements MessageModel {
}