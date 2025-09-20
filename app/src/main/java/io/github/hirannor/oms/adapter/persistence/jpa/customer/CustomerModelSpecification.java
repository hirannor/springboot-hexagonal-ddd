package io.github.hirannor.oms.adapter.persistence.jpa.customer;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Gender;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Specification aggregator for customer specification operations.
 *
 * @author Mate Karolyi
 */
final class CustomerModelSpecification {

    private CustomerModelSpecification() {
    }

    /**
     * Specification to query customer entries which were birth after the given parameter. (inclusive)
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
     * Specification to query customer entries which were birth before the given parameter. (exclusive)
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
     * Specification matcher for email emailAddress field based on the incoming parameter.
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
