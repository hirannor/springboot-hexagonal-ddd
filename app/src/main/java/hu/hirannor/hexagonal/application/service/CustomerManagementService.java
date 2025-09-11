package hu.hirannor.hexagonal.application.service;

import hu.hirannor.hexagonal.application.error.CustomerAlreadyExistWithEmailAddress;
import hu.hirannor.hexagonal.application.error.CustomerNotFound;
import hu.hirannor.hexagonal.application.usecase.*;
import hu.hirannor.hexagonal.domain.customer.*;
import hu.hirannor.hexagonal.domain.customer.command.ChangePersonalDetails;
import hu.hirannor.hexagonal.domain.customer.command.EnrollCustomer;
import hu.hirannor.hexagonal.domain.customer.query.FilterCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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
        CustomerRegistration,
        CustomerDisplaying,
        CustomerModification,
        CustomerDeletion {

    private static final Logger LOGGER = LogManager.getLogger(
            CustomerManagementService.class
    );
    private static final String ERR_CUSTOMER_ID_IS_NULL = "CustomerId cannot be null!";
    private static final String ERR_CUSTOMER_NOT_FOUND = "Customer not found with value: %s";

    private final CustomerRepository customers;

    @Autowired
    CustomerManagementService(final CustomerRepository customers) {
        this.customers = customers;
    }

    @Override
    public Customer changeBy(final ChangePersonalDetails cmd) {
        if (cmd == null) throw new IllegalArgumentException("ChangeCustomerDetails command cannot be null!");

        final Customer foundCustomer = customers.findBy(cmd.customerId())
            .orElseThrow(
                failBecauseCustomerWasNotFoundBy(cmd.customerId())
            );

        final Customer modifiedCustomer = foundCustomer.changePersonalDetailsBy(cmd);
        customers.save(modifiedCustomer);

        LOGGER.info("Personal details for customer id: {} are updated successfully!", modifiedCustomer.customerId());

        return modifiedCustomer;
    }

    @Override
    public void deleteBy(final CustomerId id) {
        if (id == null) throw new IllegalArgumentException(ERR_CUSTOMER_ID_IS_NULL);

        customers.findBy(id)
            .orElseThrow(
                failBecauseCustomerWasNotFoundBy(id)
            );

        LOGGER.info("Attempting to delete customer with id: {}", id.asText());

        customers.deleteBy(id);
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
    public Customer enroll(final EnrollCustomer command) {
        if (command == null) throw new IllegalArgumentException("EnrollCustomer command cannot be null!");

        customers.findByEmailAddress(command.emailAddress())
            .ifPresent(customer -> failBecauseCustomerAlreadyExistBy(customer.emailAddress()));

        final Customer registeredCustomer = Customer.registerBy(command);
        customers.save(registeredCustomer);

        LOGGER.info("Customer with id: {} is successfully registered!", registeredCustomer.customerId());

        return registeredCustomer;
    }

    private void failBecauseCustomerAlreadyExistBy(final EmailAddress email) {
        throw new CustomerAlreadyExistWithEmailAddress(
            String.format("Customer already exist with the given e-mail address: %s", email.value())
        );
    }

    private Supplier<CustomerNotFound> failBecauseCustomerWasNotFoundBy(final CustomerId id) {
        return () -> new CustomerNotFound(String.format(ERR_CUSTOMER_NOT_FOUND, id.asText()));
    }

}
