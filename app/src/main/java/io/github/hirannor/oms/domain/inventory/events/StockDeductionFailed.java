package io.github.hirannor.oms.domain.inventory.events;

import io.github.hirannor.oms.domain.inventory.InventoryId;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record StockDeductionFailed(
        MessageId id,
        InventoryId inventoryId,
        ProductId productId,
        int requestedQuantity,
        int reservedQuantity,
        int availableQuantity
) implements DomainEvent {

    public static StockDeductionFailed record(
            final InventoryId inventoryId,
            final ProductId productId,
            final int requestedQuantity,
            final int reservedQuantity,
            final int availableQuantity
    ) {
        return new StockDeductionFailed(
                Message.generateId(),
                inventoryId,
                productId,
                requestedQuantity,
                reservedQuantity,
                availableQuantity
        );
    }

    public static StockDeductionFailed recreate(
            final MessageId id,
            final InventoryId inventoryId,
            final ProductId productId,
            final int requestedQuantity,
            final int reservedQuantity,
            final int availableQuantity
    ) {
        return new StockDeductionFailed(
                id,
                inventoryId,
                productId,
                requestedQuantity,
                reservedQuantity,
                availableQuantity
        );
    }
}
