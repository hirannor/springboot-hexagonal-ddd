package com.hirannor.hexagonal.domain.customer;

import java.util.List;
import java.util.Optional;

/**
 * A repository interface for customer related operations.
 *
 * @author Mate Karolyi
 */
public interface CustomerRepository {

    /**
     * Retrieves all customer
     *
     * @return a list of {@link Customer} customers.
     */
    List<Customer> findAll();

    /**
     * Retrieves a customer by {@link CustomerId} id
     *
     * @param customerId {@link CustomerId} unique identifier of customer
     * @return found customer
     */
    Optional<Customer> findBy(CustomerId customerId);

    /**
     * Retrieves a customer by {@link EmailAddress} email address
     *
     * @param email {@link EmailAddress} unique email address of customer
     * @return found customer
     */
    Optional<Customer> findByEmailAddress(EmailAddress email);

    /**
     * Stores a customer
     *
     * @param customer {@link Customer} to be stored
     */
    void save(Customer customer);

    /**
     * Change details of a customer
     *
     * @param customer {@link Customer} including modified details.
     * @return {@link Customer} with modified details
     */
    Customer changeDetails(Customer customer);

}
