package hu.hirannor.hexagonal.application.service.customer;

import hu.hirannor.hexagonal.application.usecase.customer.CustomerDisplaying;
import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.Customer;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.customer.CustomerRepository;
import hu.hirannor.hexagonal.domain.customer.query.FilterCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * A service implementation of query related operations for customer
 *
 * @author Mate Karolyi
 */
@Service
@Transactional(readOnly = true)
class CustomerQueryService implements CustomerDisplaying {

    private static final Logger LOGGER = LogManager.getLogger(
        CustomerQueryService.class
    );
    private static final String ERR_CUSTOMER_ID_IS_NULL = "CustomerId cannot be null!";

    private final CustomerRepository customers;

    @Autowired
    CustomerQueryService(final CustomerRepository customers) {
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

    @Override
    public Optional<Customer> displayBy(EmailAddress emailAddress) {
        if (emailAddress == null) throw new IllegalArgumentException(ERR_CUSTOMER_ID_IS_NULL);

        LOGGER.info("Retrieving customer by id: {}", emailAddress);

        return customers.findByEmailAddress(emailAddress);
    }

}
