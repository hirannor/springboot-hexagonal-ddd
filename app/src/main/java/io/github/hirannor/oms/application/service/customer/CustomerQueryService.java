package io.github.hirannor.oms.application.service.customer;

import io.github.hirannor.oms.application.usecase.customer.CustomerDisplaying;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.CustomerRepository;
import io.github.hirannor.oms.domain.customer.query.FilterCriteria;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;


/**
 * A service implementation of query related operations for customer
 *
 * @author Mate Karolyi
 */
@ApplicationService
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

        LOGGER.info("Retrieving customer by customerId={}", customerId);

        return customers.findBy(customerId);
    }

    @Override
    public Optional<Customer> displayBy(EmailAddress emailAddress) {
        if (emailAddress == null) throw new IllegalArgumentException(ERR_CUSTOMER_ID_IS_NULL);

        LOGGER.info("Retrieving customer by emailAddress={}", emailAddress.asText());

        return customers.findByEmailAddress(emailAddress);
    }

}
