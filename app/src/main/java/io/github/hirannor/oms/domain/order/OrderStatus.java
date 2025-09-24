package io.github.hirannor.oms.domain.order;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {
    WAITING_FOR_PAYMENT,
    PAID_SUCCESSFULLY,
    PAYMENT_EXPIRED,
    PAYMENT_CANCELED,
    PAYMENT_FAILED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RETURNED,
    REFUNDED;

    public Set<OrderStatus> allowedTransitions() {
        return switch (this) {
            case PAYMENT_FAILED, PAYMENT_CANCELED -> EnumSet.of(WAITING_FOR_PAYMENT, CANCELLED, PAID_SUCCESSFULLY);
            case PAYMENT_EXPIRED -> EnumSet.of(CANCELLED);
            case WAITING_FOR_PAYMENT -> EnumSet.of(PAID_SUCCESSFULLY, PAYMENT_FAILED, PAYMENT_CANCELED, CANCELLED);
            case PAID_SUCCESSFULLY -> EnumSet.of(PROCESSING, CANCELLED, REFUNDED);
            case PROCESSING -> EnumSet.of(SHIPPED, CANCELLED);
            case SHIPPED -> EnumSet.of(DELIVERED, RETURNED);
            case DELIVERED -> EnumSet.of(RETURNED);
            case RETURNED, CANCELLED -> EnumSet.of(REFUNDED);
            case REFUNDED -> EnumSet.noneOf(OrderStatus.class);
        };
    }

    public boolean canTransitionTo(final OrderStatus next) {
        return allowedTransitions().contains(next);
    }

    public boolean isPaid() {
        return this == PAID_SUCCESSFULLY;
    }

    public boolean isShipped() {
        return this == SHIPPED;
    }

    public boolean isDelivered() {
        return this == DELIVERED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }

    public boolean isReturned() {
        return this == RETURNED;
    }

    public boolean isRefunded() {
        return this == REFUNDED;
    }

    public boolean isWaitingForPayment() {
        return this == WAITING_FOR_PAYMENT;
    }

    public boolean isExpired() {
        return this == PAYMENT_EXPIRED;
    }
}
