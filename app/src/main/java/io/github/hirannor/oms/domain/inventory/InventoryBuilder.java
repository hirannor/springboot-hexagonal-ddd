package io.github.hirannor.oms.domain.inventory;

import io.github.hirannor.oms.domain.product.ProductId;

public class InventoryBuilder {
    private InventoryId id;
    private ProductId productId;
    private int availableQuantity;
    private int reservedQuantity;

    public InventoryBuilder id(final InventoryId id) {
        this.id = id;
        return this;
    }

    public InventoryBuilder productId(final ProductId productId) {
        this.productId = productId;
        return this;
    }

    public InventoryBuilder availableQuantity(final int availableQuantity) {
        this.availableQuantity = availableQuantity;
        return this;
    }

    public InventoryBuilder reservedQuantity(final int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
        return this;
    }

    public Inventory assemble() {
        return new Inventory(id, productId, availableQuantity, reservedQuantity);
    }
}