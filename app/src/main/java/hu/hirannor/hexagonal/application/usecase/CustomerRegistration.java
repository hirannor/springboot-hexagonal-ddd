package hu.hirannor.hexagonal.application.usecase;

import hu.hirannor.hexagonal.domain.customer.Customer;
import hu.hirannor.hexagonal.domain.customer.command.EnrollCustomer;

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
