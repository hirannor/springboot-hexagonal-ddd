package io.github.hirannor.oms.adapter.messaging.eventbus.message.order;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.DomainEventModel;

public record OrderProcessingModel(
        String eventId,
        String orderId,
        String customerId
) implements DomainEventModel {
}
