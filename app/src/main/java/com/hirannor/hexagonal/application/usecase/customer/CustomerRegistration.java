package com.hirannor.hexagonal.application.usecase.customer;

import com.hirannor.hexagonal.domain.customer.RegisterCustomer;
import com.hirannor.hexagonal.domain.customer.Customer;

public interface CustomerRegistration {

    Customer register(RegisterCustomer cmd);

}
