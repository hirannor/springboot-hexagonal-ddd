package hu.hirannor.hexagonal.adapter.persistence.jpa.order;

import java.util.Objects;

public enum PaymentMethodModel {

    CARD("CARD");

    private final String dbRepresentation;

    PaymentMethodModel(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    public static PaymentMethodModel from(final String text) {
        Objects.requireNonNull(text);
        for (final PaymentMethodModel method : PaymentMethodModel.values()) {
            if (method.dbRepresentation.equalsIgnoreCase(text)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + text);
    }

    public String dbRepresentation() {
        return this.dbRepresentation;
    }
}
