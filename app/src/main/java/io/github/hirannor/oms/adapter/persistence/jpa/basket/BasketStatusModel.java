package io.github.hirannor.oms.adapter.persistence.jpa.basket;

import java.util.Objects;

public enum BasketStatusModel {

    ACTIVE("ACTIVE"),
    CHECKED_OUT("CHECKED_OUT"),
    EXPIRED("EXPIRED");

    private final String dbRepresentation;

    BasketStatusModel(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    public static BasketStatusModel from(final String text) {
        Objects.requireNonNull(text);
        for (final BasketStatusModel status : BasketStatusModel.values()) {
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
