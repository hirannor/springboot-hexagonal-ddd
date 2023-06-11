package hu.hirannor.hexagonal.domain.customer;

import hu.hirannor.hexagonal.domain.customer.query.FilterCriteria;

import java.util.List;
import java.util.Optional;

/**
 * A repository interface for customer related operations.
 *
 * @author Mate Karolyi
 */
public interface CustomerRepository {

    /**
     * Change details of a customer
     *
     * @param customer {@link Customer} including modified details.
     * @return {@link Customer} with modified details
     */
    Customer updateDetails(Customer customer);

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

}
