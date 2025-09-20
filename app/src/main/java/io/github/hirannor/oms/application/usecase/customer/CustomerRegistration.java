package io.github.hirannor.oms.application.usecase.customer;

import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.command.EnrollCustomer;

/**
 * Customer related use case for customer registration.
 *
 * @author Mate Karolyi
 */
public interface CustomerRegistration {

    /**
     * Register a customer based on the {@link EnrollCustomer} command.
     *
     * @param cmd {@link EnrollCustomer} including the registered customer
     * @return {@link Customer} registered customer
     */
    Customer enroll(EnrollCustomer cmd);

}
