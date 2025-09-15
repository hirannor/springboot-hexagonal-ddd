package hu.hirannor.hexagonal.domain.order;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {
    CREATED,
    WAITING_FOR_PAYMENT,
    PAID_SUCCESSFULLY,
    PAYMENT_CANCELED,
    PAYMENT_FAILED,
    PAYMENT_PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RETURNED,
    REFUNDED;

    public Set<OrderStatus> allowedTransitions() {
        return switch (this) {
            case CREATED -> EnumSet.of(WAITING_FOR_PAYMENT, CANCELLED);
            case PAYMENT_FAILED, PAYMENT_CANCELED ->
                    EnumSet.of(WAITING_FOR_PAYMENT, PAYMENT_PENDING, CANCELLED);
            case WAITING_FOR_PAYMENT ->
                    EnumSet.of(PAYMENT_PENDING, PAID_SUCCESSFULLY, PAYMENT_FAILED, PAYMENT_CANCELED, CANCELLED);
            case PAYMENT_PENDING ->
                    EnumSet.of(PAID_SUCCESSFULLY, PAYMENT_FAILED, PAYMENT_CANCELED, CANCELLED);
            case PAID_SUCCESSFULLY ->
                    EnumSet.of(PROCESSING, CANCELLED, REFUNDED);
            case PROCESSING ->
                    EnumSet.of(SHIPPED, CANCELLED);
            case SHIPPED ->
                    EnumSet.of(DELIVERED, RETURNED);
            case DELIVERED ->
                    EnumSet.of(RETURNED);
            case RETURNED, CANCELLED ->
                    EnumSet.of(REFUNDED);
            case REFUNDED ->
                    EnumSet.noneOf(OrderStatus.class);
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
}
