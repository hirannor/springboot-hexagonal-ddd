package io.github.hirannor.oms.domain.core.valueobject;

import java.util.UUID;

/**
 * An immutable record to hold customer's value.
 *
 * @param value {@link String} raw value of customer value.
 * @author Mate Karolyi
 */
public record CustomerId(String value) {

    public CustomerId {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("CustomerId cannot be null or empty!");
    }

    /**
     * Create an instance of {@link CustomerId} value.
     *
     * @param value {@link String} raw value of customer value
     * @return create an instance of {@link CustomerId} value
     */
    public static CustomerId from(final String value) {
        return new CustomerId(value);
    }

    /**
     * Generate a random unique identifier for customer value.
     *
     * @return create an instance of {@link CustomerId} value
     */
    public static CustomerId generate() {
        return new CustomerId(UUID.randomUUID().toString());
    }

    /**
     * Retrieves the unique identifier of a customer as a text.
     *
     * @return unique identifier as text
     */
    public String asText() {
        return this.value;
    }
}
