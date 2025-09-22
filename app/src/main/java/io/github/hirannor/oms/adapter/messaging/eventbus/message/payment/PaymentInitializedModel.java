package io.github.hirannor.oms.adapter.messaging.eventbus.message.payment;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.DomainEventModel;

import java.math.BigDecimal;

public record PaymentInitializedModel(
        String eventId,
        String paymentId,
        String orderId,
        BigDecimal amount,
        String currency
) implements DomainEventModel {}