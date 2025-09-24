package io.github.hirannor.oms.application.usecase.inventory;

import io.github.hirannor.oms.domain.inventory.Inventory;
import io.github.hirannor.oms.domain.inventory.command.CreateInventory;

public interface InventoryCreation {
    Inventory create(CreateInventory cmd);
}
