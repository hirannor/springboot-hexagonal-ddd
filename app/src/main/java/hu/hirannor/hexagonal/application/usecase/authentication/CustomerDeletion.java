package hu.hirannor.hexagonal.application.usecase.authentication;

import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;

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
