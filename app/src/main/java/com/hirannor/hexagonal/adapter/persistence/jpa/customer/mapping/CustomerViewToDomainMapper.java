package com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.AddressView;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerView;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.GenderModel;
import com.hirannor.hexagonal.domain.customer.*;

import java.util.function.Function;

class CustomerViewToDomainMapper implements Function<CustomerView, Customer> {

    private final Function<AddressView, Address> addressViewToDomain;
    private final Function<GenderModel, Gender> mapGenderModelToDomain;

    CustomerViewToDomainMapper() {
        this(new AddressViewToDomainMapper(), new GenderModelToDomainMapper());
    }

    CustomerViewToDomainMapper(final Function<AddressView, Address> addressViewToDomain,
                                Function<GenderModel, Gender> mapGenderModelToDomain) {
        this.addressViewToDomain = addressViewToDomain;
        this.mapGenderModelToDomain = mapGenderModelToDomain;
    }

    @Override
    public Customer apply(final CustomerView view) {
        if (view == null) return null;

        return Customer.from(
                CustomerId.from(view.getCustomerId()),
                FullName.from(view.getFirstName(), view.getLastName()),
                Age.from(view.getAge()),
                mapGenderModelToDomain.apply(view.getGender()),
                view.getAddresses()
                        .stream()
                        .map(addressViewToDomain)
                        .toList(),
                EmailAddress.from(view.getEmailAddress())
        );
    }

}

