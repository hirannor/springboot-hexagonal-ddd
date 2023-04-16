package com.hirannor.hexagonal.adapter.api.rest.mapping;

import com.hirannor.hexagonal.adapter.api.rest.model.AddressModel;
import com.hirannor.hexagonal.adapter.api.rest.model.CustomerModel;
import com.hirannor.hexagonal.adapter.api.rest.model.GenderModel;
import com.hirannor.hexagonal.domain.customer.Address;
import com.hirannor.hexagonal.domain.customer.Customer;
import com.hirannor.hexagonal.domain.customer.Gender;

import java.math.BigDecimal;
import java.util.function.Function;

class CustomerToModelMapper implements Function<Customer, CustomerModel> {

    private final Function<Gender, GenderModel> mapGenderToModel;
    private final Function<Address, AddressModel> addressToModel;

    CustomerToModelMapper() {
        this(new GenderToModelMapper(), new AddressToModelMapper());
    }

    CustomerToModelMapper(final Function<Gender, GenderModel> mapGenderToModel,
                          final Function<Address, AddressModel> addressToModel) {

        this.mapGenderToModel = mapGenderToModel;
        this.addressToModel = addressToModel;

    }

    @Override
    public CustomerModel apply(final Customer customer) {
        if (customer == null) return null;

        return new CustomerModel()
                .customerId(customer.customerId().value())
                .firstName(customer.fullName().firstName())
                .lastName(customer.fullName().lastName())
                .age(BigDecimal.valueOf(customer.age().value()))
                .gender(mapGenderToModel.apply(customer.gender()))
                .addresses(
                        customer.addresses()
                                .stream()
                                .map(addressToModel)
                                .toList()
                )
                .emailAddress(customer.emailAddress().value());
    }

}
