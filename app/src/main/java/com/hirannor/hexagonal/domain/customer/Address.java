package com.hirannor.hexagonal.domain.customer;

public record Address(Country country, String city, PostalCode postalCode, String streetAddress) {

    public Address {
        if (country == null || city == null || postalCode == null || streetAddress == null) {
            throw new IllegalArgumentException("Country|City|PostalCode|StreetAddress are required parameters4");
        }
    }

    public static Address from(final Country country, final String city,
                               final PostalCode postalCode, final String streetAddress) {
        return new Address(country, city, postalCode, streetAddress);
    }
}
