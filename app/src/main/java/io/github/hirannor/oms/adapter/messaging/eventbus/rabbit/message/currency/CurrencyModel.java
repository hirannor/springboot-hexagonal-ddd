package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.currency;

import java.util.Objects;

public enum CurrencyModel {

    HUF("HUF"),

    EUR("EUR");

    private final String value;

    CurrencyModel(final String value) {
        this.value = value;
    }

    public static CurrencyModel from(final String text) {
        Objects.requireNonNull(text, "Currency string cannot be null");

        for (final CurrencyModel currency : CurrencyModel.values()) {
            if (currency.value.equalsIgnoreCase(text)) {
                return currency;
            }
        }

        throw new IllegalArgumentException(
                String.format("Unexpected currency value: %s", text)
        );
    }

    public String value() {
        return this.value;
    }
}