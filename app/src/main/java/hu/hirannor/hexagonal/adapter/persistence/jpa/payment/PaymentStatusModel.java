package hu.hirannor.hexagonal.adapter.persistence.jpa.payment;

import java.util.Objects;

public enum PaymentStatusModel {

    INITIALIZED("INITIALIZED"),
    SUCCEEDED("SUCCEEDED"),
    FAILED("FAILED"),
    CANCELED("CANCELED");

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
