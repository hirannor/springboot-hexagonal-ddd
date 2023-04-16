package com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.AddressModel;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.GenderModel;
import com.hirannor.hexagonal.domain.customer.Address;
import com.hirannor.hexagonal.domain.customer.Customer;
import com.hirannor.hexagonal.domain.customer.Gender;

import java.util.function.Function;

class CustomerToModelMapper implements Function<Customer, CustomerModel> {

    private final Function<Gender, GenderModel> mapGenderToModel;
    private final Function<Address, AddressModel> mapAddressToModel;

    CustomerToModelMapper() {
        this(new GenderToModelMapper(), new AddressToModelMapper());
    }

    CustomerToModelMapper(
            final Function<Gender, GenderModel> mapGenderToModel,
            final Function<Address, AddressModel> mapAddressToModel) {

        this.mapGenderToModel = mapGenderToModel;
        this.mapAddressToModel = mapAddressToModel;
    }

    @Override
    public CustomerModel apply(final Customer domain) {
        if (domain == null) return null;

        final CustomerModel model = new CustomerModel();

        model.setCustomerId(domain.customerId().value());
        model.setGender(mapGenderToModel.apply(domain.gender()));
        model.setAge(domain.age().value());
        model.setFirstName(domain.fullName().firstName());
        model.setLastName(domain.fullName().lastName());

        model.setAddresses(domain.addresses()
                .stream()
                .map(mapAddressToModel)
                .toList());

        model.setEmailAddress(domain.emailAddress().value());

        return model;
    }

}
