package io.github.hirannor.oms.adapter.persistence.jpa.customer;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerView;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A spring data repository for {@link CustomerModel} model notificationType.
 *
 * @author Mate Karolyi
 */
interface CustomerSpringDataJpaRepository extends Repository<CustomerModel, Long> {

    /**
     * Retrieves all the customers by the given query specifications.
     *
     * @param spec query specification as {@link Specification<CustomerModel>}
     * @return an instance of {@link Page <CustomerModel>}
     */
    List<CustomerModel> findAll(Specification<CustomerModel> spec);

    /**
     * Retrieves a customer by {@link String} customer value.
     *
     * @param customerId {@link String} unique identifier of a customer
     * @return stored {@link CustomerModel} entity
     */
    Optional<CustomerModel> findByCustomerId(String customerId);

    /**
     * Stores a {@link CustomerModel} entity to the database
     *
     * @param model {@link CustomerModel} to be persisted
     */
    void save(CustomerModel model);

    /**
     * Retrieves a customer by {@link String} email emailAddress.
     *
     * @param emailAddress {@link String} unique email emailAddress of a customer
     * @return {@link CustomerView} view
     */
    Optional<CustomerView> findByEmailAddress(String emailAddress);

    /**
     * Deletes a customer by value
     *
     * @param customerId {@link String} value of customer
     */
    void deleteByCustomerId(String customerId);

}
