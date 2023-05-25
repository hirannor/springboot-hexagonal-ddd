package com.hirannor.hexagonal.application.usecase.customer;

import com.hirannor.hexagonal.domain.customer.Customer;
import com.hirannor.hexagonal.domain.customer.ChangeCustomerDetails;

public interface CustomerModification {

    Customer change(ChangeCustomerDetails cmd);
}
