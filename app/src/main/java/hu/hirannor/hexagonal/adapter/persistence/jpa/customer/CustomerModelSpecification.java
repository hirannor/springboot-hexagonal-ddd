package hu.hirannor.hexagonal.adapter.persistence.jpa.customer;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import hu.hirannor.hexagonal.domain.customer.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.Gender;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification aggregator for customer specification operations.
 *
 * @author Mate Karolyi
 */
public final class CustomerModelSpecification {

    private CustomerModelSpecification() {
    }

    /**
     * Specification to query customer entries which were birth after the given parameter. (Inclusive)
     *
     * @param from {@link LocalDate} filter from date
     * @return Specification output {@link Specification< CustomerModel >}
     */
    public static Specification<CustomerModel> birthAfter(
        final Optional<LocalDate> from
    ) {
        return from
            .map(BirthDateMatcher::after)
            .orElseGet(Ignored::create);
    }

    /**
     * Specification to query customer entries which were birth before the given parameter. (Inclusive)
     *
     * @param to {@link LocalDate} filter to date
     * @return Specification output {@link Specification< CustomerModel >}
     */
    public static Specification<CustomerModel> birthBefore(
        final Optional<LocalDate> to
    ) {
        return to
            .map(BirthDateMatcher::before)
            .orElseGet(Ignored::create);
    }

    /**
     * Specification matcher for gender field based on the incoming parameter.
     *
     * @param gender {@link Gender}
     * @return Specification output {@link Specification< CustomerModel >}
     */
    public static Specification<CustomerModel> genderMatches(
        final Optional<Gender> gender
    ) {
        return gender
            .map(GenderMatcher::matchWith)
            .orElseGet(Ignored::create);
    }

    /**
     * Specification matcher for email address field based on the incoming parameter.
     *
     * @param email {@link EmailAddress}
     * @return Specification output {@link Specification< CustomerModel >}
     */
    public static Specification<CustomerModel> emailAddressMatches(
        final Optional<EmailAddress> email
    ) {
        return email
            .map(EmailAddressMatcher::matchWith)
            .orElseGet(Ignored::create);
    }
}
