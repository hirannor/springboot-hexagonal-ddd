package io.github.hirannor.oms.adapter.persistence.jpa.outbox.inventory;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventModel;

public record StockDeductionFailedModel(
        String eventId,
        String inventoryId,
        String productId,
        int requestedQuantity,
        int reservedQuantity,
        int availableQuantity
) implements DomainEventModel {

}
