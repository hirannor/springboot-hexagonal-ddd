package com.hirannor.hexagonal.adapter.api.rest.mapping;

import com.hirannor.hexagonal.adapter.api.rest.model.*;
import com.hirannor.hexagonal.domain.customer.*;

import java.util.function.Function;

class RegisterCustomerModelToDomainMapper implements Function<RegisterCustomerModel, RegisterCustomer> {

    private final Function<AddressModel, Address> mapAddressModelToDomain;
    private final Function<GenderModel, Gender> mapGenderModelToDomain;

    RegisterCustomerModelToDomainMapper() {
        this(new AddressModelToDomainMapper(), new GenderModelToDomainMapper());
    }

    RegisterCustomerModelToDomainMapper(final Function<AddressModel, Address> mapAddressModelToDomain,
                                        final Function<GenderModel, Gender> mapGenderModelToDomain) {
        this.mapAddressModelToDomain = mapAddressModelToDomain;
        this.mapGenderModelToDomain = mapGenderModelToDomain;
    }

    @Override
    public RegisterCustomer apply(final RegisterCustomerModel model) {
        if (model == null) return null;

        return RegisterCustomer.issue(
                FullName.from(model.getFirstName(), model.getLastName()),
                model.getBirthDate(),
                mapGenderModelToDomain.apply(model.getGender()),
                mapAddressModelToDomain.apply(model.getAddress()),
                EmailAddress.from(model.getEmailAddress())
        );
    }

}
