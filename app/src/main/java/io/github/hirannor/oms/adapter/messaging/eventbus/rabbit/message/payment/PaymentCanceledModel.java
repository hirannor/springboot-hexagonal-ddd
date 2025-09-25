package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventModel;

public record PaymentCanceledModel(
        String eventId,
        String paymentId,
        String orderId
) implements DomainEventModel {
}