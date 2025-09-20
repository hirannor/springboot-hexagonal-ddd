package io.github.hirannor.oms.adapter.payment.stripe;


import java.util.Objects;

public enum CheckOutSessionEvent {

    COMPLETED("checkout.session.completed"),
    EXPIRED("checkout.session.expired"),
    ASYNC_SUCCESS("checkout.session.async_payment_succeeded"),
    ASYNC_FAILED("checkout.session.async_payment_failed");

    private final String value;

    CheckOutSessionEvent(final String value) {
        this.value = value;
    }

    public static CheckOutSessionEvent from(final String text) {
        Objects.requireNonNull(text, "Currency string cannot be null");

        for (final CheckOutSessionEvent checkOutSession : CheckOutSessionEvent.values()) {
            if (checkOutSession.value.equalsIgnoreCase(text)) {
                return checkOutSession;
            }
        }

        throw new IllegalArgumentException(
                String.format("Unexpected checkout session model value: %s", text)
        );
    }

    public String value() {
        return this.value;
    }
}