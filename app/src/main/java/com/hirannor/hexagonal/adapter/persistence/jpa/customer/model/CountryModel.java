package com.hirannor.hexagonal.adapter.persistence.jpa.customer.model;

import java.util.Objects;

public enum CountryModel {
    HUNGARY("Hungary"),

    GERMANY("Germany");

    private final String dbRepresentation;

    CountryModel(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    public static CountryModel from(final String text) {
        Objects.requireNonNull(text);

        for (final CountryModel status : CountryModel.values()) {
            if (status.dbRepresentation.equalsIgnoreCase(text)) return status;
        }

        throw new IllegalArgumentException(
                String.format("Unexpected value %s", text)
        );
    }

    public String dbRepresentation() {
        return this.dbRepresentation;
    }

}
