package com.hirannor.hexagonal.adapter.persistence.jpa.customer;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping.CustomerMappingFactory;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping.CustomerModeller;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerView;
import com.hirannor.hexagonal.domain.customer.Customer;
import com.hirannor.hexagonal.domain.customer.CustomerId;
import com.hirannor.hexagonal.domain.customer.CustomerRepository;
import com.hirannor.hexagonal.domain.customer.EmailAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
@Transactional(
        propagation = Propagation.MANDATORY,
        isolation = Isolation.REPEATABLE_READ
)
class CustomerJpaRepository implements CustomerRepository {

    private static final Logger LOGGER = LogManager.getLogger(
            CustomerJpaRepository.class
    );

    private final Function<Customer, CustomerModel> customerToModel;
    private final Function<CustomerModel, Customer> customerModelToDomain;
    private final Function<CustomerView, Customer> customerViewToDomain;
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
                          final Function<Customer, CustomerModel> customerToModel,
                          final Function<CustomerModel, Customer> customerModelToDomain,
                          final Function<CustomerView, Customer> customerViewToDomain) {
        this.customers = customers;
        this.customerToModel = customerToModel;
        this.customerModelToDomain = customerModelToDomain;
        this.customerViewToDomain = customerViewToDomain;
    }

    @Override
    public List<Customer> findAll() {
        return customers.findAllProjectedBy()
                .stream()
                .map(customerViewToDomain)
                .toList();
    }

    @Override
    public Optional<Customer> findBy(final CustomerId customerId) {
        if (customerId == null) throw new IllegalArgumentException("CustomerId cannot be null!");

        LOGGER.debug("Fetching customer by id: {}", customerId);

        return customers.findByCustomerId(customerId.value())
                .map(customerModelToDomain);
    }

    @Override
    public void save(final Customer customer) {
        if (customer == null) throw new IllegalArgumentException("Customer cannot be null!");

        LOGGER.debug("Saving customer....");

        customers.save(customerToModel.apply(customer));
    }

    @Override
    public Optional<Customer> findByEmailAddress(final EmailAddress email) {
        if (email == null) throw new IllegalArgumentException("EmailAddress cannot be null!");

        LOGGER.debug("Fetching customer by e-mail address: {}", email);

        return customers.findByEmailAddress(email.value())
                .map(customerModelToDomain);
    }

    @Override
    public Customer changeDetails(final Customer domain) {
        if (domain == null) throw new IllegalArgumentException("Customer cannot be null!");

        LOGGER.debug("Changing customer details for customer id: {}", domain.customerId());

        final String rawCustomerId = domain.customerId().value();

        final CustomerModel model = customers.findByCustomerId(rawCustomerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found for id: " + rawCustomerId));

        final CustomerModel modifiedCustomer = CustomerModeller.applyChangesFrom(domain).to(model);
        customers.save(modifiedCustomer);

        return domain;
    }
}
