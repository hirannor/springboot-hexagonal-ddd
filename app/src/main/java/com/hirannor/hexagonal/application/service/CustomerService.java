package com.hirannor.hexagonal.application.service;

import com.hirannor.hexagonal.application.usecase.customer.CustomerDisplay;
import com.hirannor.hexagonal.application.usecase.customer.CustomerRegistration;
import com.hirannor.hexagonal.application.usecase.eventing.EventPublishing;
import com.hirannor.hexagonal.domain.customer.AddCustomer;
import com.hirannor.hexagonal.domain.customer.Customer;
import com.hirannor.hexagonal.domain.customer.CustomerId;
import com.hirannor.hexagonal.domain.customer.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(
        propagation = Propagation.REQUIRES_NEW,
        isolation = Isolation.REPEATABLE_READ
)
class CustomerService implements
        CustomerRegistration,
        CustomerDisplay {

    private static final Logger LOGGER = LogManager.getLogger(
            CustomerService.class
    );

    private final CustomerRepository customers;
    private final EventPublishing events;

    @Autowired
    CustomerService(final CustomerRepository customers, final EventPublishing events) {
        this.customers = customers;
        this.events = events;
    }

    @Override
    public Customer signup(final AddCustomer command) {
        if (command == null) throw new IllegalArgumentException("AddCustomer command cannot be null!");

        final Optional<Customer> customerFound = customers.findByEmailAddress(command.emailAddress());

        if (customerFound.isPresent()) {
            throw new IllegalArgumentException("Customer already exist with the given e-mail address:" + command.emailAddress());
        }

        final Customer registeredCustomer = Customer.register(command);

        customers.save(registeredCustomer);
        events.publish(registeredCustomer.listEvents());

        registeredCustomer.clearEvents();

        LOGGER.info("Customer with id: {} is successfully saved!", registeredCustomer.customerId());

        return registeredCustomer;
    }

    @Override
    public List<Customer> displayAll() {
        LOGGER.info("Retrieving all customer...");

        return customers.findAll();
    }

    @Override
    public Optional<Customer> displayById(final CustomerId customerId) {
        LOGGER.info("Retrieving customer by id: {}", customerId);

        return customers.findBy(customerId);
    }
}
