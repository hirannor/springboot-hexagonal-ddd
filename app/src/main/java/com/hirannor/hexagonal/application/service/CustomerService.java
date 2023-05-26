package com.hirannor.hexagonal.application.service;

import com.hirannor.hexagonal.application.port.messaging.MessagePublisher;
import com.hirannor.hexagonal.application.usecase.CustomerDisplay;
import com.hirannor.hexagonal.application.usecase.CustomerModification;
import com.hirannor.hexagonal.application.usecase.CustomerEnrolling;
import com.hirannor.hexagonal.domain.customer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * A service implementation of customer related use cases.
 *
 * @author Mate Karolyi
 */
@Service
@Transactional(
        propagation = Propagation.REQUIRES_NEW,
        isolation = Isolation.REPEATABLE_READ
)
class CustomerService implements
        CustomerEnrolling,
        CustomerDisplay,
        CustomerModification {

    private static final Logger LOGGER = LogManager.getLogger(
            CustomerService.class
    );

    private final CustomerRepository customers;
    private final MessagePublisher messages;

    @Autowired
    CustomerService(final CustomerRepository customers, final MessagePublisher messages) {
        this.customers = customers;
        this.messages = messages;
    }

    @Override
    public Customer register(final RegisterCustomer command) {
        if (command == null) throw new IllegalArgumentException("RegisterCustomer command cannot be null!");

        final Optional<Customer> existingCustomer = customers.findByEmailAddress(command.emailAddress());

        if (existingCustomer.isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Customer already exist with the given e-mail address: %s", command.emailAddress())
            );
        }

        final Customer newCustomer = Customer.registerBy(command);
        customers.save(newCustomer);

        messages.publish(newCustomer.listEvents());
        newCustomer.clearEvents();

        LOGGER.info("Customer with customerId: {} is successfully registered!", newCustomer.customerId());

        return newCustomer;
    }

    @Override
    public List<Customer> displayAll() {
        LOGGER.info("Retrieving all customers...");

        return customers.findAll();
    }

    @Override
    public Optional<Customer> displayBy(final CustomerId customerId) {
        LOGGER.info("Retrieving customer by customerId: {}", customerId);

        return customers.findBy(customerId);
    }

    @Override
    public Customer changeDetails(final ChangeCustomerDetails cmd) {
        final Customer existingCustomer = customers.findBy(cmd.customerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("Customer not found with customerId: %s ", cmd.customerId()))
                );

        existingCustomer.changeDetailsBy(cmd);
        customers.changeDetails(existingCustomer);

        messages.publish(existingCustomer.listEvents());
        existingCustomer.clearEvents();

        LOGGER.info("Customer details for customer id: {} are changed successfully!", existingCustomer.customerId());

        return existingCustomer;
    }
}
