package com.hirannor.hexagonal.domain.customer;

public record PostalCode(Integer value) {

    public PostalCode {
        if (value == null) throw new IllegalArgumentException("PostalCode cannot be null!");
    }

    public static PostalCode from(final Integer value) {
        return new PostalCode(value);
    }

}
