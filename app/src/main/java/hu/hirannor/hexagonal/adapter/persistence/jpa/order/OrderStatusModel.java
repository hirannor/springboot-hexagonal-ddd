package hu.hirannor.hexagonal.adapter.persistence.jpa.order;

import java.util.Objects;

public enum OrderStatusModel {

    CREATED("CREATED"),
    WAITING_FOR_PAYMENT("WAITING_FOR_PAYMENT"),
    PAYMENT_PENDING("PAYMENT_PENDING"),
    PAID_SUCCESSFULLY("PAID_SUCCESSFULLY"),
    PAYMENT_FAILED("PAYMENT_FAILED"),
    PAYMENT_CANCELED("PAYMENT_CANCELED"),
    PROCESSING("PROCESSING"),
    SHIPPED("SHIPPED"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED"),
    RETURNED("RETURNED"),
    REFUNDED("REFUNDED");

    private final String dbRepresentation;

    OrderStatusModel(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    public static OrderStatusModel from(final String text) {
        Objects.requireNonNull(text);
        for (final OrderStatusModel status : OrderStatusModel.values()) {
            if (status.dbRepresentation.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + text);
    }

    public String dbRepresentation() {
        return this.dbRepresentation;
    }
}
