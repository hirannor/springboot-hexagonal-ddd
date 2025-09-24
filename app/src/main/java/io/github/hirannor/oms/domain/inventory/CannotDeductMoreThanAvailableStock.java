package io.github.hirannor.oms.domain.inventory;

public class CannotDeductMoreThanAvailableStock extends RuntimeException {
    public CannotDeductMoreThanAvailableStock(final String message) {
        super(message);
    }
}
