package io.github.hirannor.oms.domain.customer;


public record FirstName(String value) {
    public FirstName {
        if (value == null)
            throw new IllegalArgumentException("Firstname cannot be null!");
    }

    public static FirstName from(final String value) {
        return new FirstName(value);
    }

    public String asText() {
        return value;
    }
}
