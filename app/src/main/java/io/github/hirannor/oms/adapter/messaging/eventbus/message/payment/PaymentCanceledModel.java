package io.github.hirannor.oms.adapter.messaging.eventbus.message.payment;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.DomainEventModel;

public record PaymentCanceledModel(
        String eventId,
        String paymentId,
        String orderId
) implements DomainEventModel {}