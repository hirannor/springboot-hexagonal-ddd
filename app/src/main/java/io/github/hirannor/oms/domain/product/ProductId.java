package io.github.hirannor.oms.domain.product;

import java.util.UUID;

/**
 * An immutable record to hold productId's value.
 *
 * @param value {@link String} raw value of productId value.
 * @author Mate Karolyi
 */
public record ProductId(String value) {

    public ProductId {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("ProductId cannot be null or empty!");
    }


    public static ProductId from(final String value) {
        return new ProductId(value);
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID().toString());
    }

    public String asText() {
        return this.value;
    }
}
