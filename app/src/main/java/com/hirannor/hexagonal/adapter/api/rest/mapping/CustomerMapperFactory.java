package com.hirannor.hexagonal.adapter.api.rest.mapping;

import com.hirannor.hexagonal.adapter.api.rest.model.CustomerModel;
import com.hirannor.hexagonal.adapter.api.rest.model.SignupRequestModel;
import com.hirannor.hexagonal.domain.customer.AddCustomer;
import com.hirannor.hexagonal.domain.customer.Customer;

import java.util.function.Function;

public interface CustomerMapperFactory {

    static Function<Customer, CustomerModel> createCustomerToModelMapper() {
        return new CustomerToModelMapper();
    }

    static Function<SignupRequestModel, AddCustomer> createSignupRequestModelToAddCustomerMapper() {
        return new SignUpRequestModelToAddCustomerMapper();
    }

}
