package hu.hirannor.hexagonal.application.usecase;

import hu.hirannor.hexagonal.domain.customer.CustomerId;

/**
 * Use case for customer deletion.
 *
 * @author Mate Karolyi
 */
public interface CustomerDeletion {

    /**
     * Deletes a customer by value
     *
     * @param customerId {@link CustomerId} value of customer
     */
    void deleteBy(CustomerId customerId);

}
