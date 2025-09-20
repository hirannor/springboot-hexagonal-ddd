package io.github.hirannor.oms.domain.payment;

import java.util.UUID;


public record PaymentId(String value) {

    public PaymentId {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("PaymentId cannot be null or empty!");
    }

    public static PaymentId from(final String value) {
        return new PaymentId(value);
    }

    public static PaymentId unknown() {
        return new PaymentId("Unknown");
    }

    public static PaymentId generate() {
        return new PaymentId(UUID.randomUUID().toString());
    }

    public String asText() {
        return this.value;
    }
}
