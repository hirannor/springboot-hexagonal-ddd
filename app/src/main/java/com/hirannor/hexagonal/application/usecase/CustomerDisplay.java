package com.hirannor.hexagonal.application.usecase;

import com.hirannor.hexagonal.domain.customer.Customer;
import com.hirannor.hexagonal.domain.customer.CustomerId;
import java.util.List;
import java.util.Optional;

/**
 * Customer related use case for displaying customers.
 *
 * @author Mate Karolyi
 */
public interface CustomerDisplay {

    /**
     * Displays all available customers.
     *
     * @return a list of {@link Customer} customers.
     */
    List<Customer> displayAll();

    /**
     * Displays a customer by {@link CustomerId} id.
     *
     * @param customerId {@link CustomerId} unique identifier of a customer
     * @return found customer
     */
    Optional<Customer> displayBy(CustomerId customerId);

}
