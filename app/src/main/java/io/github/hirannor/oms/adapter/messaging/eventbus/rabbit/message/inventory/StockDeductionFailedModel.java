package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.inventory;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventModel;

public record StockDeductionFailedModel(
        String eventId,
        String inventoryId,
        String productId,
        int requestedQuantity,
        int reservedQuantity,
        int availableQuantity
) implements DomainEventModel {

}
