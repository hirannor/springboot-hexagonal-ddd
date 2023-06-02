package hu.hirannor.hexagonal.application.service;

import hu.hirannor.hexagonal.application.error.CustomerAlreadyExist;
import hu.hirannor.hexagonal.application.error.CustomerNotFound;
import hu.hirannor.hexagonal.application.port.messaging.MessagePublisher;
import hu.hirannor.hexagonal.application.usecase.*;
import hu.hirannor.hexagonal.domain.customer.*;
import hu.hirannor.hexagonal.domain.customer.command.ChangeCustomerDetails;
import hu.hirannor.hexagonal.domain.customer.command.RegisterCustomer;
import hu.hirannor.hexagonal.domain.customer.query.FilterCriteria;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

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
class CustomerManagementService implements
    CustomerEnrolling,
    CustomerDisplay,
    CustomerModification,
    CustomerDeletion {

    private static final Logger LOGGER = LogManager.getLogger(
        CustomerManagementService.class
    );
    private static final String ERR_CUSTOMER_ID_IS_NULL = "CustomerId cannot be null!";
    private static final String ERR_CUSTOMER_NOT_FOUND = "Customer not found with id: %s";

    private final CustomerRepository customers;
    private final MessagePublisher messages;

    @Autowired
    CustomerManagementService(final CustomerRepository customers,
                              final MessagePublisher messages) {
        this.customers = customers;
        this.messages = messages;
    }

    @Override
    public Customer changeDetailsBy(final ChangeCustomerDetails cmd) {
        if (cmd == null) throw new IllegalArgumentException("ChangeCustomerDetails command cannot be null!");

        final Customer foundCustomer = customers.findBy(cmd.customerId())
            .orElseThrow(
                () -> new CustomerNotFound(String.format(ERR_CUSTOMER_NOT_FOUND, cmd.customerId().asText()))
            );

        final Customer updatedCustomer = foundCustomer.changeDetailsBy(cmd);
        customers.updateDetails(updatedCustomer);

        messages.publish(updatedCustomer.listEvents());
        updatedCustomer.clearEvents();

        LOGGER.info("Customer details for customer id: {} are updated successfully!", updatedCustomer.customerId());

        return updatedCustomer;
    }

    @Override
    public void deleteBy(final CustomerId customerId) {
        if (customerId == null) throw new IllegalArgumentException(ERR_CUSTOMER_ID_IS_NULL);

        customers.findBy(customerId)
            .orElseThrow(
                () -> new CustomerNotFound(String.format(ERR_CUSTOMER_NOT_FOUND, customerId.asText()))
            );

        LOGGER.info("Attempting to delete customer by id: {}", customerId.asText());

        customers.deleteBy(customerId);
    }

    @Override
    public List<Customer> displayAllBy(final FilterCriteria criteria) {
        if (criteria == null) throw new IllegalArgumentException("FilterCriteria object cannot be null!");

        LOGGER.info("Retrieving customers...");

        return customers.findAllBy(criteria);
    }

    @Override
    public Optional<Customer> displayBy(final CustomerId customerId) {
        if (customerId == null) throw new IllegalArgumentException(ERR_CUSTOMER_ID_IS_NULL);

        LOGGER.info("Retrieving customer by id: {}", customerId);

        return customers.findBy(customerId);
    }

    @Override
    public Customer register(final RegisterCustomer command) {
        if (command == null) throw new IllegalArgumentException("RegisterCustomer command cannot be null!");

        final Optional<Customer> existingCustomer = customers.findByEmailAddress(command.emailAddress());

        if (existingCustomer.isPresent()) {
            throw new CustomerAlreadyExist(
                String.format("Customer already exist with the given e-mail address: %s", command.emailAddress())
            );
        }

        final Customer newCustomer = Customer.registerBy(command);
        customers.save(newCustomer);

        messages.publish(newCustomer.listEvents());
        newCustomer.clearEvents();

        LOGGER.info("Customer with id: {} is successfully registered!", newCustomer.customerId());

        return newCustomer;
    }

}
