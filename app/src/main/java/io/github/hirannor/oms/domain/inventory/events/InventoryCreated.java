package io.github.hirannor.oms.domain.inventory.events;

import io.github.hirannor.oms.domain.inventory.InventoryId;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record InventoryCreated(
        MessageId id,
        InventoryId inventoryId,
        ProductId productId,
        int initialQuantity
) implements DomainEvent {

    public static InventoryCreated record(
            final InventoryId inventoryId,
            final ProductId productId,
            final int initialQuantity
    ) {
        return new InventoryCreated(Message.generateId(), inventoryId, productId, initialQuantity);
    }

    public static InventoryCreated recreate(
            final MessageId id,
            final InventoryId inventoryId,
            final ProductId productId,
            final int initialQuantity
    ) {
        return new InventoryCreated(id, inventoryId, productId, initialQuantity);
    }
}
