package com.hirannor.hexagonal.adapter.persistence.jpa.customer;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping.CustomerMappingFactory;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping.CustomerModeller;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerView;
import com.hirannor.hexagonal.domain.customer.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.*;

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
class CustomerJpaRepository implements CustomerRepository {

    private static final Logger LOGGER = LogManager.getLogger(
        CustomerJpaRepository.class
    );

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
    public List<Customer> findAll() {
        return customers.findAllProjectedBy()
            .stream()
            .map(mapViewToDomain)
            .toList();
    }

    @Override
    public Optional<Customer> findBy(final CustomerId customerId) {
        if (customerId == null) throw new IllegalArgumentException("CustomerId cannot be null!");

        LOGGER.debug("Fetching customer by id: {}", customerId);

        return customers.findByCustomerId(customerId.value())
            .map(mapModelToDomain);
    }

    @Override
    public void save(final Customer customer) {
        if (customer == null) throw new IllegalArgumentException("Customer cannot be null!");

        LOGGER.debug("Saving customer....");

        customers.save(mapDomainToModel.apply(customer));
    }

    @Override
    public Optional<Customer> findByEmailAddress(final EmailAddress email) {
        if (email == null) throw new IllegalArgumentException("EmailAddress cannot be null!");

        LOGGER.debug("Fetching customer by e-mail address: {}", email);

        return customers.findByEmailAddress(email.value())
            .map(mapModelToDomain);
    }

    @Override
    public Customer changeDetails(final Customer domain) {
        if (domain == null) throw new IllegalArgumentException("Customer cannot be null!");

        LOGGER.debug("Changing customer details for customer id: {}", domain.customerId());

        final String rawCustomerId = domain.customerId().value();

        final CustomerModel model = customers.findByCustomerId(rawCustomerId)
            .orElseThrow(() -> new IllegalArgumentException(String.format("Customer not found with id: %s ", rawCustomerId)));

        final CustomerModel modifiedCustomer = CustomerModeller.applyChangesFrom(domain).to(model);
        customers.save(modifiedCustomer);

        return domain;
    }
}
