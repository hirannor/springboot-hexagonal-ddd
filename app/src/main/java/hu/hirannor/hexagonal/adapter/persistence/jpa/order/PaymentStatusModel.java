package hu.hirannor.hexagonal.adapter.persistence.jpa.order;

import java.util.Objects;

public enum PaymentStatusModel {

    PENDING("PENDING"),
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    CANCELLED("CANCELLED");

    private final String dbRepresentation;

    PaymentStatusModel(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    public static PaymentStatusModel from(final String text) {
        Objects.requireNonNull(text);
        for (final PaymentStatusModel status : PaymentStatusModel.values()) {
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
