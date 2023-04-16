package com.hirannor.hexagonal.domain.customer;


import java.util.UUID;

public record CustomerId(String value) {

    public CustomerId {
        if (value == null || value.isEmpty()) throw new IllegalArgumentException("CustomerId cannot be null!");
    }

    public static CustomerId from(final String value) {
        return new CustomerId(value);
    }

    public static CustomerId generate() {
        return new CustomerId(UUID.randomUUID().toString());
    }

    public String asText() {
        return this.value;
    }
}
