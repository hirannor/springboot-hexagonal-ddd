package io.github.hirannor.oms.adapter.payment.stripe;

import java.util.Objects;

public enum PaymentIntentEvent {

    PAYMENT_SUCCEEDED("payment_intent.succeeded"),
    PAYMENT_FAILED("payment_intent.payment_failed"),
    PAYMENT_CANCELED("payment_intent.canceled");

    private final String value;

    PaymentIntentEvent(final String value) {
        this.value = value;
    }

    public static PaymentIntentEvent from(final String text) {
        Objects.requireNonNull(text, "Currency string cannot be null");

        for (final PaymentIntentEvent event : PaymentIntentEvent.values()) {
            if (event.value.equalsIgnoreCase(text)) {
                return event;
            }
        }

        throw new IllegalArgumentException(
                String.format("Unexpected payment event value: %s", text)
        );
    }

    public String value() {
        return this.value;
    }
}