package com.hirannor.hexagonal.adapter.api.rest.mapping;


import com.hirannor.hexagonal.adapter.api.rest.model.AddressModel;
import com.hirannor.hexagonal.adapter.api.rest.model.GenderModel;
import com.hirannor.hexagonal.adapter.api.rest.model.SignupRequestModel;
import com.hirannor.hexagonal.domain.customer.*;

import java.util.function.Function;

class SignUpRequestModelToAddCustomerMapper implements Function<SignupRequestModel, AddCustomer> {

    private final Function<AddressModel, Address> mapAddressModelToDomain;
    private final Function<GenderModel, Gender> mapGenderModelToDomain;

    SignUpRequestModelToAddCustomerMapper() {
        this(new AddressModelToDomainMapper(), new GenderModelToDomainMapper());
    }

    SignUpRequestModelToAddCustomerMapper(final Function<AddressModel, Address> mapAddressModelToDomain,
                                          final Function<GenderModel, Gender> mapGenderModelToDomain) {
        this.mapAddressModelToDomain = mapAddressModelToDomain;
        this.mapGenderModelToDomain = mapGenderModelToDomain;
    }

    @Override
    public AddCustomer apply(final SignupRequestModel model) {
        if (model == null) return null;

        return AddCustomer.issue(
                FullName.from(model.getFirstName(), model.getLastName()),
                Age.from(model.getAge().intValue()),
                mapGenderModelToDomain.apply(model.getGender()),
                model.getAddresses()
                        .stream()
                        .map(mapAddressModelToDomain)
                        .toList(),
                EmailAddress.from(model.getEmailAddress())
        );
    }

}
