package com.hirannor.hexagonal.application.usecase.customer;

import com.hirannor.hexagonal.domain.customer.Customer;
import com.hirannor.hexagonal.domain.customer.CustomerId;

import java.util.List;
import java.util.Optional;

public interface CustomerDisplay {

    List<Customer> displayAll();

    Optional<Customer> displayBy(CustomerId customerId);

}
