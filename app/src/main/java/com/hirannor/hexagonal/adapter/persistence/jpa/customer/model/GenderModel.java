package com.hirannor.hexagonal.adapter.persistence.jpa.customer.model;

import java.util.Objects;

public enum GenderModel {

    MALE("Male"),

    FEMALE("Female");

    private final String dbRepresentation;

    GenderModel(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    public static GenderModel from(final String text) {
        Objects.requireNonNull(text);

        for (final GenderModel status : GenderModel.values()) {
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
