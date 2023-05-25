package com.hirannor.hexagonal.domain.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    List<Customer> findAll();

    Optional<Customer> findBy(CustomerId customerId);

    Optional<Customer> findByEmailAddress(EmailAddress email);

    void save(Customer customer);

    Customer changeDetails(Customer customer);

}
