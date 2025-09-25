package io.github.hirannor.oms.domain.inventory.error;

public class InventoryReservationFailed extends RuntimeException {
    public InventoryReservationFailed(final String message) {
        super(message);
    }
}
