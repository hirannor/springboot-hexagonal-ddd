package io.github.hirannor.oms.domain.customer;

/**
 * Immutable record to hold full name of customer
 *
 * @param firstName {@link String}
 * @param lastName  {@link String}
 * @author Mate Karolyi
 */
public record FullName(String firstName, String lastName) {

    /**
     * Default constructor
     *
     * @param firstName
     * @param lastName
     */
    public FullName {
        if (firstName == null || lastName == null)
            throw new IllegalArgumentException("Firstname or Lastname cannot be null!");
    }

    /**
     * Create an instance of {@link FullName} based on the given parameters
     *
     * @param firstName
     * @param lastName
     * @return an instance of {@link FullName}
     */
    public static FullName from(final String firstName, final String lastName) {
        return new FullName(firstName, lastName);
    }

    public String concatFirstAndLastName() {
        return this.firstName + " " + this.lastName;
    }

}
