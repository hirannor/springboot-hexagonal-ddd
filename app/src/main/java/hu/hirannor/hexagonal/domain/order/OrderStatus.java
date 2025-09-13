package hu.hirannor.hexagonal.domain.order;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {
    CREATED,
    WAITING_FOR_PAYMENT,
    PAID,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RETURNED,
    REFUNDED;

    public Set<OrderStatus> allowedTransitions() {
        return switch (this) {
            case CREATED -> EnumSet.of(WAITING_FOR_PAYMENT, CANCELLED);
            case WAITING_FOR_PAYMENT -> EnumSet.of(PAID, CANCELLED);
            case PAID -> EnumSet.of(PROCESSING, CANCELLED, REFUNDED);
            case PROCESSING -> EnumSet.of(SHIPPED, CANCELLED);
            case SHIPPED -> EnumSet.of(DELIVERED, RETURNED);
            case DELIVERED -> EnumSet.of(RETURNED);
            case RETURNED -> EnumSet.of(REFUNDED);
            case REFUNDED, CANCELLED -> EnumSet.noneOf(OrderStatus.class);
        };
    }

    public boolean canTransitionTo(final OrderStatus next) {
        return allowedTransitions().contains(next);
    }

    public boolean isPaid() {
        return this == PAID;
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
}
