package com.hirannor.hexagonal.application.usecase.customer;

import com.hirannor.hexagonal.domain.customer.AddCustomer;
import com.hirannor.hexagonal.domain.customer.Customer;

public interface CustomerRegistration {

    Customer signup(AddCustomer cmd);

}
