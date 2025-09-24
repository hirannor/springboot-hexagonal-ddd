package io.github.hirannor.oms.domain.inventory;

public class CannotDeductMoreThanReserved extends RuntimeException {
    public CannotDeductMoreThanReserved(final String message) {
        super(message);
    }
}
