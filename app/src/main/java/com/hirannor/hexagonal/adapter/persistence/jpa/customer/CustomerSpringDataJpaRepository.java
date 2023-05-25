package com.hirannor.hexagonal.adapter.persistence.jpa.customer;


import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerView;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.*;

/**
 * A spring data repository for {@link CustomerModel} model type.
 *
 * @author Mate Karolyi
 */
@Transactional(
    propagation = Propagation.MANDATORY,
    isolation = Isolation.REPEATABLE_READ
)
interface CustomerSpringDataJpaRepository extends Repository<CustomerModel, Long> {

    /**
     * Retrieves all customer from database
     *
     * @return a list of {@link CustomerView}
     */
    List<CustomerView> findAllProjectedBy();

    /**
     * Retrieves a customer by {@link String} customer id.
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
     * Retrieves a customer by {@link String} email address.
     *
     * @param emailAddress {@link String} unique email address of a customer
     * @return stored {@link CustomerModel} entity
     */
    Optional<CustomerModel> findByEmailAddress(String emailAddress);

}
