package com.hirannor.hexagonal.application.usecase;

import com.hirannor.hexagonal.domain.customer.Customer;
import com.hirannor.hexagonal.domain.customer.RegisterCustomer;

/**
 * Customer related use case for customer registration.
 *
 * @author Mate Karolyi
 */
public interface CustomerRegistration {

    /**
     * Register a customer based on the {@link RegisterCustomer} command.
     *
     * @param cmd {@link RegisterCustomer} including the registered customer
     * @return {@link Customer} registered customer
     */
    Customer register(RegisterCustomer cmd);

}
