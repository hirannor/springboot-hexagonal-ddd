package com.hirannor.hexagonal.adapter.persistence.jpa.customer;


import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerView;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(
        propagation = Propagation.MANDATORY,
        isolation = Isolation.REPEATABLE_READ
)
interface CustomerSpringDataJpaRepository  extends Repository<CustomerModel, Long> {

    List<CustomerView> findAllProjectedBy();

    Optional<CustomerModel> findByCustomerId(String customerId);

    void save(CustomerModel model);

    Optional<CustomerModel> findByEmailAddress(String emailAddress);

}
