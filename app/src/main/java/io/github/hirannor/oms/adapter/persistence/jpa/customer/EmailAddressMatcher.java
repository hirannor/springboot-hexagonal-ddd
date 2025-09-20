package io.github.hirannor.oms.adapter.persistence.jpa.customer;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel_;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;

/**
 * Specification implementation for matching a given email emailAddress.
 *
 * @author Mate Karolyi
 */
final class EmailAddressMatcher implements Specification<CustomerModel> {

    @Serial
    private static final long serialVersionUID = -3_344_013_656_839_255_788L;

    private final String emailAddressToMatchWith;

    private EmailAddressMatcher(final String emailAddressToMatchWith) {
        this.emailAddressToMatchWith = emailAddressToMatchWith;
    }

    static Specification<CustomerModel> matchWith(
            final EmailAddress email
    ) {
        return new EmailAddressMatcher(email.value());
    }

    @Override
    public Predicate toPredicate(
            final Root<CustomerModel> root,
            final CriteriaQuery<?> query,
            final CriteriaBuilder cb
    ) {
        return cb.equal(root.get(CustomerModel_.EMAIL_ADDRESS), emailAddressToMatchWith);
    }
}
