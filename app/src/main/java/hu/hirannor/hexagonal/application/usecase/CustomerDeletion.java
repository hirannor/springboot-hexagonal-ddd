package hu.hirannor.hexagonal.application.usecase;

import hu.hirannor.hexagonal.domain.customer.CustomerId;

/**
 * Use case for customer deletion.
 *
 * @author Mate Karolyi
 */
public interface CustomerDeletion {

    /**
     * Deletes a customer by id
     *
     * @param customerId {@link CustomerId} id of customer
     */
    void deleteBy(CustomerId customerId);

}
