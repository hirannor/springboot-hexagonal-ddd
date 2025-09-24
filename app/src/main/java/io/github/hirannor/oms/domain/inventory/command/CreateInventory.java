package io.github.hirannor.oms.domain.inventory.command;

import io.github.hirannor.oms.domain.inventory.InventoryId;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

public record CreateInventory(
        CommandId id,
        InventoryId inventoryId,
        ProductId productId,
        int initialQuantity
) implements Command {

    public static CreateInventory issue(
            final ProductId productId,
            final int initialQuantity
    ) {
        return new CreateInventory(
                CommandId.generate(),
                InventoryId.generate(),
                productId,
                initialQuantity
        );
    }
}
