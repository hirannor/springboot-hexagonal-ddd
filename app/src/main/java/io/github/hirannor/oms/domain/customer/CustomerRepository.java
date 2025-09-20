package io.github.hirannor.oms.domain.customer;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.query.FilterCriteria;

import java.util.List;
import java.util.Optional;

/**
 * A repository interface for customer related operations.
 *
 * @author Mate Karolyi
 */
public interface CustomerRepository {

    /**
     * Deletes a customer by value
     *
     * @param customerId {@link CustomerId} value of customer
     */
    void deleteBy(CustomerId customerId);

    /**
     * Retrieves all customer
     *
     * @param query {@link FilterCriteria} criteria filter
     * @return a list of {@link Customer} customers.
     */
    List<Customer> findAllBy(FilterCriteria query);

    /**
     * Retrieves a customer by {@link CustomerId} value
     *
     * @param customerId {@link CustomerId} unique identifier of customer
     * @return found customer
     */
    Optional<Customer> findBy(CustomerId customerId);

    /**
     * Retrieves a customer by {@link EmailAddress} email emailAddress
     *
     * @param email {@link EmailAddress} unique email emailAddress of customer
     * @return found customer
     */
    Optional<Customer> findByEmailAddress(EmailAddress email);

    /**
     * Stores a customer
     *
     * @param customer {@link Customer} to be stored
     */
    void save(Customer customer);

}
