package com.hirannor.hexagonal.domain.customer;

public record FullName(String firstName, String lastName) {

    public FullName {
        if (firstName == null || firstName.isBlank() ||
            lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Firstname or Lastname cannot be null or empty!");
    }

    public static FullName from(final String firstName, final String lastName) {
        return new FullName(firstName, lastName);
    }

}
