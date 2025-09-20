package io.github.hirannor.oms.domain.payment;

public enum PaymentStatus {
    INITIALIZED,
    SUCCEEDED,
    FAILED,
    CANCELED;

    public boolean canTransitionTo(final PaymentStatus target) {
        return switch (this) {
            case INITIALIZED -> target == SUCCEEDED || target == FAILED || target == CANCELED;
            case FAILED -> target == SUCCEEDED || target == CANCELED;
            case SUCCEEDED, CANCELED -> false;
        };
    }
}
