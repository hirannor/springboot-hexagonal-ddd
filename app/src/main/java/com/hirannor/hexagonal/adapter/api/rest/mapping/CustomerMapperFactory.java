package com.hirannor.hexagonal.adapter.api.rest.mapping;

import com.hirannor.hexagonal.adapter.api.rest.model.*;
import com.hirannor.hexagonal.domain.customer.*;

import java.util.function.Function;

public interface CustomerMapperFactory {

    static Function<Customer, CustomerModel> createCustomerToModelMapper() {
        return new CustomerToModelMapper();
    }

    static Function<RegisterCustomerModel, RegisterCustomer> createRegisterCustomerModelToDomainMapper() {
        return new RegisterCustomerModelToDomainMapper();
    }

    static Function<GenderModel, Gender> createGenderModelToDomainMapper() {
        return new GenderModelToDomainMapper();
    }

    static Function<AddressModel, Address> createAddressModelToAddressMapper() {
        return new AddressModelToDomainMapper();
    }

}
