package io.github.hirannor.oms.application.usecase.customer;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.query.FilterCriteria;

import java.util.List;
import java.util.Optional;

/**
 * Customer related use case for displaying customers.
 *
 * @author Mate Karolyi
 */
public interface CustomerDisplaying {

    /**
     * Displays all available customers.
     *
     * @param criteria {@link FilterCriteria} criteria to filter
     * @return a list of {@link Customer} customers.
     */
    List<Customer> displayAllBy(FilterCriteria criteria);

    /**
     * Displays a customer by {@link CustomerId} value.
     *
     * @param customerId {@link CustomerId} unique identifier of a customer
     * @return found customer
     */
    Optional<Customer> displayBy(CustomerId customerId);

    /**
     * Displays a customer by {@link CustomerId} value.
     *
     * @param emailAddress {@link CustomerId} unique identifier of a customer
     * @return found customer
     */
    Optional<Customer> displayBy(EmailAddress emailAddress);

}
