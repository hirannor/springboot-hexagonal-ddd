package io.github.hirannor.oms.adapter.persistence.jpa.inventory.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.inventory.InventoryModel;
import io.github.hirannor.oms.domain.inventory.Inventory;
import io.github.hirannor.oms.infrastructure.modelling.Modeller;

public class InventoryModeller implements Modeller<InventoryModel> {

    private final Inventory domain;

    public InventoryModeller(final Inventory domain) {
        this.domain = domain;
    }

    public static InventoryModeller applyChangesFrom(final Inventory domain) {
        return new InventoryModeller(domain);
    }

    @Override
    public InventoryModel to(final InventoryModel from) {
        if (from == null) return null;

        from.setInventoryId(domain.id().asText());
        from.setProductId(domain.productId().asText());
        from.setAvailableQuantity(domain.availableQuantity());
        from.setReservedQuantity(domain.reservedQuantity());
        from.setCreatedAt(domain.createdAt());

        return from;
    }
}