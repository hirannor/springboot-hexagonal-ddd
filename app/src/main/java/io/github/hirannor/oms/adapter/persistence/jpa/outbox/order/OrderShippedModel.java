package io.github.hirannor.oms.adapter.persistence.jpa.outbox.order;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventModel;

public record OrderShippedModel(
        String eventId,
        String orderId,
        String customerId
) implements DomainEventModel {
}
