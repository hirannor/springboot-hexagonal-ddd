package io.github.hirannor.oms.adapter.payment.stripe;

import java.util.Objects;

public enum PaymentMethodModel {

    CARD("CARD");

    private final String value;

    PaymentMethodModel(final String value) {
        this.value = value;
    }

    public static PaymentMethodModel from(final String text) {
        Objects.requireNonNull(text);
        for (final PaymentMethodModel method : PaymentMethodModel.values()) {
            if (method.value.equalsIgnoreCase(text)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + text);
    }

    public String value() {
        return this.value;
    }
}
