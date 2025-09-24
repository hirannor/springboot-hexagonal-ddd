package io.github.hirannor.oms.adapter.persistence.jpa.inventory.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.inventory.InventoryModel;
import io.github.hirannor.oms.domain.inventory.Inventory;
import io.github.hirannor.oms.domain.inventory.InventoryId;
import io.github.hirannor.oms.domain.product.ProductId;

import java.util.function.Function;

public class InventoryModelToDomainMapper implements Function<InventoryModel, Inventory> {

    @Override
    public Inventory apply(final InventoryModel model) {
        if (model == null) return null;

        return Inventory
                .empty()
                .id(InventoryId.from(model.getInventoryId()))
                .productId(ProductId.from(model.getProductId()))
                .availableQuantity(model.getAvailableQuantity())
                .reservedQuantity(model.getReservedQuantity())
                .assemble();
    }
}
