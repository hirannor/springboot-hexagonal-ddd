package io.github.hirannor.oms.domain.customer;


public record LastName(String value) {
    public LastName {
        if (value == null)
            throw new IllegalArgumentException("LastName cannot be null!");
    }

    public static LastName from(final String value) {
        return new LastName(value);
    }

    public String asText() {
        return value;
    }
}
