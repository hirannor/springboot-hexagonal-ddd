package hu.hirannor.hexagonal.adapter.persistence.jpa.customer;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import jakarta.persistence.criteria.*;
import java.io.Serial;
import org.springframework.data.jpa.domain.Specification;

/**
 * Always true specification for ignoring query result.
 *
 * @author Mate Karolyi
 */
final class Ignored implements Specification<CustomerModel> {

    @Serial
    private static final long serialVersionUID = -4880621570493810901L;

    private Ignored() {
    }

    static Specification<CustomerModel> create() {
        return new Ignored();
    }

    @Override
    public Predicate toPredicate(
            final Root<CustomerModel> root,
            final CriteriaQuery<?> query,
            final CriteriaBuilder criteriaBuilder
    ) {
        return criteriaBuilder.conjunction();
    }
}
