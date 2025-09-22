package io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventModel;

public record PaymentFailedModel(
        String eventId,
        String paymentId,
        String orderId
) implements DomainEventModel {}