package com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.AddressModel;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.GenderModel;
import com.hirannor.hexagonal.domain.customer.*;

import java.util.function.Function;

class CustomerModelToDomainMapper implements Function<CustomerModel, Customer> {

    private final Function<AddressModel, Address> addressModelToDomain;
    private final Function<GenderModel, Gender> mapGenderModelToDomain;

    CustomerModelToDomainMapper() {
        this(new AddressModelToDomainMapper(), new GenderModelToDomainMapper());
    }

    CustomerModelToDomainMapper(final Function<AddressModel, Address> addressModelToDomain,
                                Function<GenderModel, Gender> mapGenderModelToDomain) {
        this.addressModelToDomain = addressModelToDomain;
        this.mapGenderModelToDomain = mapGenderModelToDomain;
    }

    @Override
    public Customer apply(final CustomerModel model) {
        if (model == null) return null;

        return Customer.from(
                CustomerId.from(model.getCustomerId()),
                FullName.from(model.getFirstName(), model.getLastName()),
                Age.from(model.getAge()),
                mapGenderModelToDomain.apply(model.getGender()),
                model.getAddresses()
                        .stream()
                        .map(addressModelToDomain)
                        .toList(),
                EmailAddress.from(model.getEmailAddress())
        );
    }

}
