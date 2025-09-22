package io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventModel;

import java.math.BigDecimal;

public record PaymentInitializedModel(
        String eventId,
        String paymentId,
        String orderId,
        BigDecimal amount,
        String currency
) implements DomainEventModel {}