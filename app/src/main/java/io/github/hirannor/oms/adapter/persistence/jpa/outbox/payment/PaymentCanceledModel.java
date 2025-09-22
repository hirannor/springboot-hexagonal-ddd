package io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventModel;

public record PaymentCanceledModel(
        String eventId,
        String paymentId,
        String orderId
) implements DomainEventModel {
}