package hu.hirannor.hexagonal.application.usecase;

import hu.hirannor.hexagonal.domain.customer.Customer;
import hu.hirannor.hexagonal.domain.customer.command.RegisterCustomer;

/**
 * Customer related use case for customer registration.
 *
 * @author Mate Karolyi
 */
public interface CustomerEnrolling {

    /**
     * Register a customer based on the {@link RegisterCustomer} command.
     *
     * @param cmd {@link RegisterCustomer} including the registered customer
     * @return {@link Customer} registered customer
     */
    Customer register(RegisterCustomer cmd);

}
