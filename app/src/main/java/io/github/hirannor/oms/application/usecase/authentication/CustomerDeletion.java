package io.github.hirannor.oms.application.usecase.authentication;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;

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
