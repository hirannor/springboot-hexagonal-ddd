package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventModel;

public record OrderShippedModel(
        String eventId,
        String orderId,
        String customerId
) implements DomainEventModel {
}
