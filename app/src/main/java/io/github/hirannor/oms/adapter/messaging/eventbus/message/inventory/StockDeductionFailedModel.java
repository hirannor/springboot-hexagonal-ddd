package io.github.hirannor.oms.adapter.messaging.eventbus.message.inventory;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.DomainEventModel;

public record StockDeductionFailedModel(
        String eventId,
        String inventoryId,
        String productId,
        int requestedQuantity,
        int reservedQuantity,
        int availableQuantity
) implements DomainEventModel {

}
