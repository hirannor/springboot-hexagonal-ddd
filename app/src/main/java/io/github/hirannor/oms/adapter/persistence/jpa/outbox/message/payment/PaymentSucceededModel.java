package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModel;

import java.math.BigDecimal;

public record PaymentSucceededModel(
        String id,
        String paymentId,
        String orderId,
        BigDecimal amount,
        String currency
) implements MessageModel {
}