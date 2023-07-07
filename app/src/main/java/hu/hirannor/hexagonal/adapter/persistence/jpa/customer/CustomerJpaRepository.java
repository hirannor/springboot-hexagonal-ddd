package hu.hirannor.hexagonal.adapter.persistence.jpa.customer;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping.CustomerMappingFactory;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping.CustomerModeller;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerView;
import hu.hirannor.hexagonal.application.error.CustomerNotFound;
import hu.hirannor.hexagonal.domain.customer.*;
import hu.hirannor.hexagonal.domain.customer.query.FilterCriteria;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static hu.hirannor.hexagonal.adapter.persistence.jpa.customer.CustomerModelSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Spring JPA implementation of {@link CustomerRepository} repository.
 *
 * @author Mate Karolyi
 */
@Repository
@Transactional(
        propagation = Propagation.MANDATORY,
        isolation = Isolation.REPEATABLE_READ
)
@DrivenAdapter
class CustomerJpaRepository implements CustomerRepository {

    private static final Logger LOGGER = LogManager.getLogger(
            CustomerJpaRepository.class
    );
    private static final String ERR_CUSTOMER_ID_IS_NULL = "CustomerId cannot be null!";
    private static final String ERR_CUSTOMER_IS_NULL = "Customer cannot be null!";

    private final Function<Customer, CustomerModel> mapDomainToModel;
    private final Function<CustomerModel, Customer> mapModelToDomain;
    private final Function<CustomerView, Customer> mapViewToDomain;

    private final CustomerSpringDataJpaRepository customers;

    @Autowired
    CustomerJpaRepository(final CustomerSpringDataJpaRepository customers) {
        this(customers,
                CustomerMappingFactory.createCustomerToModelMapper(),
                CustomerMappingFactory.createCustomerModelToDomainMapper(),
                CustomerMappingFactory.createCustomerViewToDomainMapper()
        );
    }

    CustomerJpaRepository(final CustomerSpringDataJpaRepository customers,
                          final Function<Customer, CustomerModel> mapDomainToModel,
                          final Function<CustomerModel, Customer> mapModelToDomain,
                          final Function<CustomerView, Customer> mapViewToDomain) {
        this.customers = customers;
        this.mapDomainToModel = mapDomainToModel;
        this.mapModelToDomain = mapModelToDomain;
        this.mapViewToDomain = mapViewToDomain;
    }

    @Override
    public Customer updateDetails(final Customer domain) {
        if (domain == null) throw new IllegalArgumentException(ERR_CUSTOMER_IS_NULL);

        LOGGER.debug("Changing customer details for customer id: {}", domain.customerId());

        final CustomerModel model = customers.findByCustomerId(domain.customerId().asText())
                .orElseThrow(
                        () -> new CustomerNotFound("Customer not found with id: " + domain)
                );

        final CustomerModel modifiedCustomer = CustomerModeller.applyChangesFrom(domain).to(model);
        customers.save(modifiedCustomer);

        return domain;
    }

    @Override
    public void deleteBy(final CustomerId id) {
        if (id == null) throw new IllegalArgumentException(ERR_CUSTOMER_ID_IS_NULL);

        LOGGER.debug("Attempting to delete customer by id: {}", id);

        customers.deleteByCustomerId(id.asText());
    }

    @Override
    public List<Customer> findAllBy(final FilterCriteria criteria) {
        if (criteria == null) throw new IllegalArgumentException("FilterCriteria criteria object cannot be null!");

        return customers.findAll(
                        where(
                                emailAddressMatches(criteria.email())
                                        .and(genderMatches(criteria.gender()))
                                        .and(birthAfter(criteria.birthDateFrom()))
                                        .and(birthBefore(criteria.birthDateToToExclusive()))
                        )
                ).stream()
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
        if (email == null) throw new IllegalArgumentException("EmailAddress cannot be null!");

        LOGGER.debug("Fetching customer by e-mail address: {}", email);

        return customers.findByEmailAddress(email.value())
                .map(mapViewToDomain);
    }

    @Override
    public void save(final Customer domain) {
        if (domain == null) throw new IllegalArgumentException(ERR_CUSTOMER_IS_NULL);

        LOGGER.debug("Saving customer....");

        final CustomerModel model = mapDomainToModel.apply(domain);
        customers.save(model);
    }

}
