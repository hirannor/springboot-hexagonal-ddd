package io.github.hirannor.oms.adapter.persistence.jpa.customer;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;

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
