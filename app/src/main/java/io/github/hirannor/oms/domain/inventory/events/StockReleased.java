package io.github.hirannor.oms.domain.inventory.events;

import io.github.hirannor.oms.domain.inventory.InventoryId;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record StockReleased(
        MessageId id,
        InventoryId inventoryId,
        ProductId productId,
        int quantity,
        int reservedQuantity,
        int freeToSell
) implements DomainEvent {

    public static StockReleased record(
            final InventoryId inventoryId,
            final ProductId productId,
            final int quantity,
            final int reservedQuantity,
            final int freeToSell
    ) {
        return new StockReleased(Message.generateId(), inventoryId, productId, quantity, reservedQuantity, freeToSell);
    }

    public static StockReleased recreate(
            final MessageId id,
            final InventoryId inventoryId,
            final ProductId productId,
            final int quantity,
            final int reservedQuantity,
            final int freeToSell
    ) {
        return new StockReleased(id, inventoryId, productId, quantity, reservedQuantity, freeToSell);
    }
}
