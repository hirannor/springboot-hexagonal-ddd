package io.github.hirannor.oms.adapter.persistence.jpa;

import java.util.Objects;

public enum CurrencyModel {

    HUF("HUF"),

    EUR("EUR");

    private final String dbRepresentation;

    CurrencyModel(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    public static CurrencyModel from(final String text) {
        Objects.requireNonNull(text, "Currency string cannot be null");

        for (final CurrencyModel currency : CurrencyModel.values()) {
            if (currency.dbRepresentation.equalsIgnoreCase(text)) {
                return currency;
            }
        }

        throw new IllegalArgumentException(
                String.format("Unexpected currency value: %s", text)
        );
    }

    public String dbRepresentation() {
        return this.dbRepresentation;
    }
}