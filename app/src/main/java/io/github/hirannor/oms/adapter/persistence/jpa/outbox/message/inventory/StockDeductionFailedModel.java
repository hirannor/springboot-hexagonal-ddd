package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.inventory;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModel;

public record StockDeductionFailedModel(
        String id,
        String inventoryId,
        String productId,
        int requestedQuantity,
        int reservedQuantity,
        int availableQuantity
) implements MessageModel {

}
