package io.github.hirannor.oms.domain.order;

import java.util.UUID;


/**
 * An immutable record to hold orderId's value.
 *
 * @param value {@link String} raw value of orderId value.
 * @author Mate Karolyi
 */
public record OrderId(String value) {

    public OrderId {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("OrderId cannot be null or empty!");
    }

    public static OrderId from(final String value) {
        return new OrderId(value);
    }

    public static OrderId unknown() {
        return new OrderId("Unknown");
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID().toString());
    }

    public String asText() {
        return this.value;
    }
}
