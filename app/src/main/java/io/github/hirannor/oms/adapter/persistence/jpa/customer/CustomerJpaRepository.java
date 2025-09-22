package io.github.hirannor.oms.adapter.persistence.jpa.customer;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.mapping.CustomerMappingFactory;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.mapping.CustomerModeller;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerView;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.CustomerRepository;
import io.github.hirannor.oms.domain.customer.query.FilterCriteria;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import io.github.hirannor.oms.infrastructure.adapter.PersistenceAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.github.hirannor.oms.adapter.persistence.jpa.customer.CustomerModelSpecification.*;

/**
 * Spring JPA implementation of {@link CustomerRepository} repository.
 *
 * @author Mate Karolyi
 */
@DrivenAdapter
@PersistenceAdapter
class CustomerJpaRepository implements CustomerRepository {

    private static final Logger LOGGER = LogManager.getLogger(
            CustomerJpaRepository.class
    );
    private static final String ERR_CUSTOMER_ID_IS_NULL = "CustomerId cannot be null!";
    private static final String ERR_CUSTOMER_IS_NULL = "Customer cannot be null!";
    private static final String ERR_EMAIL_ADDRESS_CANNOT_IS_NULL = "EmailAddress cannot be null!";

    private final Function<CustomerModel, Customer> mapModelToDomain;
    private final Function<CustomerView, Customer> mapViewToDomain;

    private final CustomerSpringDataJpaRepository customers;

    @Autowired
    CustomerJpaRepository(final CustomerSpringDataJpaRepository customers) {
        this(customers,
                CustomerMappingFactory.createCustomerModelToDomainMapper(),
                CustomerMappingFactory.createCustomerViewToDomainMapper()
        );
    }

    CustomerJpaRepository(final CustomerSpringDataJpaRepository customers,
                          final Function<CustomerModel, Customer> mapModelToDomain,
                          final Function<CustomerView, Customer> mapViewToDomain) {
        this.customers = customers;
        this.mapModelToDomain = mapModelToDomain;
        this.mapViewToDomain = mapViewToDomain;
    }

    @Override
    public void deleteBy(final CustomerId id) {
        if (id == null) throw new IllegalArgumentException(ERR_CUSTOMER_ID_IS_NULL);

        LOGGER.debug("Attempting to delete customer with id: {}", id);

        customers.deleteByCustomerId(id.asText());
    }

    @Override
    public List<Customer> findAllBy(final FilterCriteria criteria) {
        if (criteria == null) throw new IllegalArgumentException("FilterCriteria object cannot be null!");

        return customers.findAll((emailAddressMatches(criteria.email())
                                .and(genderMatches(criteria.gender()))
                                .and(birthAfter(criteria.birthDateFrom()))
                                .and(birthBefore(criteria.birthDateToToExclusive()))
                        )
                )
                .stream()
                .map(mapModelToDomain)
                .toList();
    }

    @Override
    public Optional<Customer> findBy(final CustomerId id) {
        if (id == null) throw new IllegalArgumentException(ERR_CUSTOMER_ID_IS_NULL);

        LOGGER.debug("Fetching customer by id: {}", id);

        return customers.findByCustomerId(id.value())
                .map(mapModelToDomain);
    }

    @Override
    public Optional<Customer> findByEmailAddress(final EmailAddress email) {
        if (email == null) throw new IllegalArgumentException(ERR_EMAIL_ADDRESS_CANNOT_IS_NULL);

        LOGGER.debug("Fetching customer by e-mail emailAddress: {}", email);

        return customers.findByEmailAddress(email.value())
                .map(mapViewToDomain);
    }

    @Override
    public void save(final Customer domain) {
        if (domain == null) throw new IllegalArgumentException(ERR_CUSTOMER_IS_NULL);

        LOGGER.debug("Saving customer....");

        final CustomerModel toPersist = customers.findByCustomerId(domain.id().asText())
                .orElseGet(CustomerModel::new);

        CustomerModeller.applyChangesFrom(domain).to(toPersist);
        customers.save(toPersist);

        LOGGER.debug("Customer with id: {} is successfully saved!", toPersist.getCustomerId());
    }

}
