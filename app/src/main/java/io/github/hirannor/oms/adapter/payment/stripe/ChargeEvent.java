package io.github.hirannor.oms.adapter.payment.stripe;


import java.util.Objects;

public enum ChargeEvent {

    SUCCEEDED("charge.succeeded"),
    FAILED("charge.failed"),
    REFUNDED("charge.refunded"),
    DISPUTE_CREATED("charge.dispute.created"),
    DISPUTE_CLOSED("charge.dispute.closed");

    private final String value;

    ChargeEvent(final String value) {
        this.value = value;
    }

    public static ChargeEvent from(final String text) {
        Objects.requireNonNull(text, "Currency string cannot be null");

        for (final ChargeEvent event : ChargeEvent.values()) {
            if (event.value.equalsIgnoreCase(text)) {
                return event;
            }
        }

        throw new IllegalArgumentException(
                String.format("Unexpected charge event value: %s", text)
        );
    }

    public String value() {
        return this.value;
    }
}