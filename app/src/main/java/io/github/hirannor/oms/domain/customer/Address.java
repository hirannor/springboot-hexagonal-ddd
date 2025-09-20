package io.github.hirannor.oms.domain.customer;

/**
 * Immutable record to hold emailAddress details of a customer.
 *
 * @param country       {@link Country}
 * @param city          {@link String}
 * @param postalCode    {@link PostalCode}
 * @param streetAddress {@link String}
 * @author Mate Karolyi
 */
public record Address(Country country, String city, PostalCode postalCode, String streetAddress) {

    /**
     * Default constructor
     *
     * @param country       {@link Country}
     * @param city          {@link String}
     * @param postalCode    {@link PostalCode}
     * @param streetAddress {@link String}
     */
    public Address {
        if (country == null || city == null ||
                postalCode == null || streetAddress == null) {
            throw new IllegalArgumentException("Country|City|PostalCode|StreetAddress are required parameters!");
        }
    }

    /**
     * Create an instance of {@link Address} based on the parameters.
     *
     * @param country       {@link Country}
     * @param city          {@link String}
     * @param postalCode    {@link PostalCode}
     * @param streetAddress {@link String}
     * @return an instance of {@link Address} emailAddress.
     */
    public static Address from(final Country country,
                               final String city,
                               final PostalCode postalCode,
                               final String streetAddress) {
        return new Address(
                country,
                city,
                postalCode,
                streetAddress
        );
    }

    public boolean isComplete() {
        return country != null
                && city != null && !city.isBlank()
                && postalCode != null
                && streetAddress != null && !streetAddress.isBlank();
    }

    public static Address empty() {
        return new Address(null, "", PostalCode.empty(), "");
    }
}
