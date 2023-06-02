package hu.hirannor.hexagonal.adapter.persistence.jpa.customer;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel_;
import hu.hirannor.hexagonal.domain.customer.EmailAddress;
import jakarta.persistence.criteria.*;
import java.io.Serial;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification implementation for matching a given email address.
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
