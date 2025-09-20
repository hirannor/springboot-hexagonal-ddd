package io.github.hirannor.oms.adapter.persistence.jpa.customer;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.time.LocalDate;

/**
 * Specification implementation for birthdate matcher.
 * It handles the after operation as inclusive and the before as exclusive.
 *
 * @author Mate Karolyi
 */
final class BirthDateMatcher implements Specification<CustomerModel> {

    @Serial
    private static final long serialVersionUID = -5367273193915953690L;

    private final LocalDate birthDate;
    private final Timing timing;

    private BirthDateMatcher(
            final LocalDate birthDate,
            final Timing timing
    ) {
        this.birthDate = birthDate;
        this.timing = timing;
    }

    static Specification<CustomerModel> after(final LocalDate from) {
        return new BirthDateMatcher(from, Timing.AFTER);
    }

    static Specification<CustomerModel> before(final LocalDate to) {
        return new BirthDateMatcher(to, Timing.BEFORE);
    }

    @Override
    public Predicate toPredicate(
            final Root<CustomerModel> root,
            final CriteriaQuery<?> query,
            final CriteriaBuilder criteriaBuilder
    ) {
        switch (timing) {
            case AFTER -> {
                return isAfter(root, criteriaBuilder);
            }
            case BEFORE -> {
                return isBefore(root, criteriaBuilder);
            }
            default -> throw new IllegalStateException(
                    String.format("No enum constant found for: " + timing)
            );
        }
    }

    private Predicate isBefore(
            final Root<CustomerModel> root,
            final CriteriaBuilder criteriaBuilder
    ) {
        return criteriaBuilder.lessThan(
                root.get(CustomerModel_.BIRTH_DATE),
                birthDate
        );
    }

    private Predicate isAfter(
            final Root<CustomerModel> root,
            final CriteriaBuilder criteriaBuilder
    ) {
        return criteriaBuilder.greaterThanOrEqualTo(
                root.get(CustomerModel_.BIRTH_DATE),
                birthDate
        );
    }

    /**
     * Timing enumeration to identify whether after or before filter is necessary
     */
    private enum Timing {
        /**
         * After representation of timing
         */
        AFTER,

        /**
         * Before representation of timing
         */
        BEFORE,
    }
}
