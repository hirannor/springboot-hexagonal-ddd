package com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerView;
import com.hirannor.hexagonal.domain.customer.Customer;

import java.util.function.Function;

public interface CustomerMappingFactory {

    static Function<Customer, CustomerModel> createCustomerToModelMapper() {
        return new CustomerToModelMapper();
    }

    static Function<CustomerModel, Customer> createCustomerModelToDomainMapper() {
        return new CustomerModelToDomainMapper();
    }

    static Function<CustomerView, Customer> createCustomerViewToDomainMapper() {
        return new CustomerViewToDomainMapper();
    }

}
