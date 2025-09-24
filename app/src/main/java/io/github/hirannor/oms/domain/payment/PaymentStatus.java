package io.github.hirannor.oms.domain.payment;

public enum PaymentStatus {
    INITIALIZED,
    SUCCEEDED,
    FAILED,
    EXPIRED,
    CANCELED;

    public boolean canTransitionTo(final PaymentStatus target) {
        return switch (this) {
            case INITIALIZED -> target == SUCCEEDED || target == FAILED || target == CANCELED || target == EXPIRED;
            case FAILED -> target == SUCCEEDED || target == CANCELED;
            case CANCELED, SUCCEEDED -> false;
            case EXPIRED -> target == CANCELED;
        };
    }
}
