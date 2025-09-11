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
 * A service implementation of command related operations for customer management
 *
 * @author Mate Karolyi
 */
@Service
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ
)
class CustomerManagementCommandService implements
        CustomerRegistration,
        CustomerModification,
        CustomerDeletion {

    private static final Logger LOGGER = LogManager.getLogger(
        CustomerManagementCommandService.class
    );
    private static final String ERR_CUSTOMER_ID_IS_NULL = "CustomerId cannot be null!";
    private static final String ERR_CUSTOMER_NOT_FOUND = "Customer not found with value: %s";

    private final CustomerRepository customers;

    @Autowired
    CustomerManagementCommandService(final CustomerRepository customers) {
        this.customers = customers;
    }

    @Override
    public Customer changePersonalDetailsBy(final ChangePersonalDetails cmd) {
        if (cmd == null) throw new IllegalArgumentException("ChangeCustomerDetails command cannot be null!");

        final Customer foundCustomer = customers.findBy(cmd.customerId())
                .orElseThrow(
                    failBecauseCustomerWasNotFoundBy(cmd.customerId())
                );

        final Customer withModifiedPersonalDetails = foundCustomer.changePersonalDetailsBy(cmd);
        customers.save(withModifiedPersonalDetails);

        LOGGER.info("Personal details for customer id: {} are updated successfully!", withModifiedPersonalDetails.customerId());

        return withModifiedPersonalDetails;
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
    public Customer enroll(final EnrollCustomer command) {
        if (command == null) throw new IllegalArgumentException("EnrollCustomer command cannot be null!");

        customers.findByEmailAddress(command.emailAddress())
                .ifPresent(customer -> failBecauseCustomerAlreadyExistBy(customer.emailAddress()));

        final Customer newlyRegistered = Customer.registerBy(command);
        customers.save(newlyRegistered);

        LOGGER.info("Customer with id: {} is successfully registered!", newlyRegistered.customerId());

        return newlyRegistered;
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
