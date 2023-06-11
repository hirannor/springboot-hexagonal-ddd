package hu.hirannor.hexagonal.adapter.persistence.jpa.customer;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping.CustomerMappingFactory;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.*;
import hu.hirannor.hexagonal.domain.customer.Gender;
import jakarta.persistence.criteria.*;
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