package io.github.hirannor.oms.adapter.messaging.eventbus.message.payment;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.DomainEventModel;

public record PaymentFailedModel(
        String eventId,
        String paymentId,
        String orderId
) implements DomainEventModel {}