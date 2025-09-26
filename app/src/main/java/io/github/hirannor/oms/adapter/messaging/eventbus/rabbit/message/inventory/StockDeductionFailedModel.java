package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.inventory;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModel;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record StockDeductionFailedModel(
        MessageId id,
        String inventoryId,
        String productId,
        int requestedQuantity,
        int reservedQuantity,
        int availableQuantity
) implements MessageModel {

}
