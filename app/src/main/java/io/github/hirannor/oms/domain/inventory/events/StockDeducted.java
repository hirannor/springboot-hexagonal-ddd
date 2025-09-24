package io.github.hirannor.oms.domain.inventory.events;

import io.github.hirannor.oms.domain.inventory.InventoryId;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record StockDeducted(
        MessageId id,
        InventoryId inventoryId,
        ProductId productId,
        int quantity,
        int availableQuantity,
        int reservedQuantity
) implements DomainEvent {

    public static StockDeducted record(
            final InventoryId inventoryId,
            final ProductId productId,
            final int quantity,
            final int availableQuantity,
            final int reservedQuantity
    ) {
        return new StockDeducted(Message.generateId(), inventoryId, productId, quantity, availableQuantity, reservedQuantity);
    }

    public static StockDeducted recreate(
            final MessageId id,
            final InventoryId inventoryId,
            final ProductId productId,
            final int quantity,
            final int availableQuantity,
            final int reservedQuantity
    ) {
        return new StockDeducted(id, inventoryId, productId, quantity, availableQuantity, reservedQuantity);
    }
}
