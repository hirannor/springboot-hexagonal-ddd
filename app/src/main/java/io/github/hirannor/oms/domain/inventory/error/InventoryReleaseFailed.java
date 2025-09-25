package io.github.hirannor.oms.domain.inventory.error;

public class InventoryReleaseFailed extends RuntimeException {
    public InventoryReleaseFailed(final String message) {
        super(message);
    }
}
