package io.github.hirannor.oms.domain.inventory;

public class NotEnoughStockToReserve extends RuntimeException {
    public NotEnoughStockToReserve(final String message) {
        super(message);
    }
}
