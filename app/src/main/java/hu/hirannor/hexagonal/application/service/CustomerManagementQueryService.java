package hu.hirannor.hexagonal.application.service;

import hu.hirannor.hexagonal.application.error.CustomerAlreadyExistWithEmailAddress;
import hu.hirannor.hexagonal.application.error.CustomerNotFound;
import hu.hirannor.hexagonal.application.usecase.CustomerDeletion;
import hu.hirannor.hexagonal.application.usecase.CustomerDisplaying;
import hu.hirannor.hexagonal.application.usecase.CustomerModification;
import hu.hirannor.hexagonal.application.usecase.CustomerRegistration;
import hu.hirannor.hexagonal.domain.customer.Customer;
import hu.hirannor.hexagonal.domain.customer.CustomerId;
import hu.hirannor.hexagonal.domain.customer.CustomerRepository;
import hu.hirannor.hexagonal.domain.customer.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.command.ChangePersonalDetails;
import hu.hirannor.hexagonal.domain.customer.command.EnrollCustomer;
import hu.hirannor.hexagonal.domain.customer.query.FilterCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * A service implementation of query related operations for customer management
 *
 * @author Mate Karolyi
 */
@Service
@Transactional(readOnly = true)
class CustomerManagementQueryService implements
        CustomerDisplaying{

    private static final Logger LOGGER = LogManager.getLogger(
        CustomerManagementQueryService.class
    );
    private static final String ERR_CUSTOMER_ID_IS_NULL = "CustomerId cannot be null!";

    private final CustomerRepository customers;

    @Autowired
    CustomerManagementQueryService(final CustomerRepository customers) {
        this.customers = customers;
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

}
