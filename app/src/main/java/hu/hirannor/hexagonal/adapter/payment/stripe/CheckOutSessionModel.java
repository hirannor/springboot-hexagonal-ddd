package hu.hirannor.hexagonal.adapter.payment.stripe;


import java.util.Objects;

public enum CheckOutSessionModel {

    COMPLETED("checkout.session.completed"),

    EXPIRED("checkout.session.expired"),

    FAILED("checkout.session.async_payment_failed");

    private final String value;

    CheckOutSessionModel(final String value) {
        this.value = value;
    }

    public static CheckOutSessionModel from(final String text) {
        Objects.requireNonNull(text, "Currency string cannot be null");

        for (final CheckOutSessionModel checkOutSession : CheckOutSessionModel.values()) {
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