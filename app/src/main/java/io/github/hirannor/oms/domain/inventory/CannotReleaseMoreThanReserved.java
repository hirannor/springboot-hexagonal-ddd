package io.github.hirannor.oms.domain.inventory;

public class CannotReleaseMoreThanReserved extends RuntimeException {
    public CannotReleaseMoreThanReserved(final String message) {
        super(message);
    }
}
