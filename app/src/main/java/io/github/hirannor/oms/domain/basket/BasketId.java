package io.github.hirannor.oms.domain.basket;

import java.util.UUID;

public record BasketId(String value) {

    public BasketId {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("BasketId cannot be null or empty!");
    }

    public static BasketId from(final String value) {
        return new BasketId(value);
    }

    public static BasketId generate() {
        return new BasketId(UUID.randomUUID().toString());
    }

    public String asText() {
        return this.value;
    }
}
