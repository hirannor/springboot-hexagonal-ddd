package com.hirannor.hexagonal.domain.customer;

public record Age(int value) {

    public Age {
        if (value <= 0 || value >= 120 ) throw new IllegalArgumentException("Age must between 1 and 120!");
    }

    public static Age from(final int value) {
        return new Age(value);
    }
}
