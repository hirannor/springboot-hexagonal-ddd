package io.github.hirannor.oms.adapter.persistence.jpa.customer.model;

import java.util.Objects;

/**
 * Enumeration representation of available countries
 *
 * @author Mate Karolyi
 */
public enum CountryModel {
    HUNGARY("Hungary"),

    GERMANY("Germany");

    private final String dbRepresentation;

    CountryModel(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    /**
     * Retrieves the enumeration based on the given input
     *
     * @param text {@link String} db representation of enumeration value
     * @return {@link CountryModel} found enumeration model
     */
    public static CountryModel from(final String text) {
        Objects.requireNonNull(text);

        for (final CountryModel status : CountryModel.values()) {
            if (status.dbRepresentation.equalsIgnoreCase(text)) return status;
        }

        throw new IllegalArgumentException(
                String.format("Unexpected value %s", text)
        );
    }

    /**
     * Retrieves the db representation of {@link CountryModel} enumeration.
     *
     * @return {@link String} db representation of enumeration.
     */
    public String dbRepresentation() {
        return this.dbRepresentation;
    }

}
