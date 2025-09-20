package io.github.hirannor.oms.adapter.persistence.jpa.customer.model;

import java.util.Objects;

/**
 * Enumeration representation of available genders.
 *
 * @author Mate Karolyi
 */
public enum GenderModel {

    MALE("Male"),

    FEMALE("Female");

    private final String dbRepresentation;

    GenderModel(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    /**
     * Retrieves the enumeration based on the given input.
     *
     * @param text {@link String} db representation of enumeration value
     * @return {@link GenderModel} found enumeration model
     */
    public static GenderModel from(final String text) {
        Objects.requireNonNull(text);

        for (final GenderModel status : GenderModel.values()) {
            if (status.dbRepresentation.equalsIgnoreCase(text)) return status;
        }

        throw new IllegalArgumentException(
                String.format("Unexpected value %s", text)
        );
    }

    /**
     * Retrieves the db representation of {@link GenderModel} enumeration.
     *
     * @return {@link String} db representation of enumeration.
     */
    public String dbRepresentation() {
        return this.dbRepresentation;
    }

}
