package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventModel;

public record OrderCreatedModel(
        String eventId,
        String orderId,
        String customerId
) implements DomainEventModel {
}
