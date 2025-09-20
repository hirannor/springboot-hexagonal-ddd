package io.github.hirannor.oms.adapter.persistence.jpa.customer;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.mapping.CustomerMappingFactory;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel_;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.GenderModel;
import io.github.hirannor.oms.domain.customer.Gender;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.util.function.Function;

/**
 * Specification implementation for matching a given gender.
 *
 * @author Mate Karolyi
 */
final class GenderMatcher implements Specification<CustomerModel> {

    @Serial
    private static final long serialVersionUID = 5584609959823320470L;
    private final GenderModel genderMatchWith;

    private GenderMatcher(final GenderModel genderMatchWith) {
        this.genderMatchWith = genderMatchWith;
    }

    static Specification<CustomerModel> matchWith(
            final Gender genderMatchWith
    ) {
        final Function<Gender, GenderModel> mapDomainToModel = CustomerMappingFactory.createGenderToModelMapper();
        final GenderModel model = mapDomainToModel.apply(genderMatchWith);

        return new GenderMatcher(model);
    }

    @Override
    public Predicate toPredicate(final Root<CustomerModel> root,
                                 final CriteriaQuery<?> query,
                                 final CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get(CustomerModel_.GENDER), genderMatchWith);
    }
}