package com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CountryModel;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.GenderModel;
import com.hirannor.hexagonal.domain.customer.*;

import java.util.function.Function;

class CustomerModelToDomainMapper implements Function<CustomerModel, Customer> {

    private final Function<CountryModel, Country> countryModelToDomain;
    private final Function<GenderModel, Gender> mapGenderModelToDomain;

    CustomerModelToDomainMapper() {
        this(new CountryModelToDomainMapper(), new GenderModelToDomainMapper());
    }

    CustomerModelToDomainMapper(final Function<CountryModel, Country> countryModelToDomain,
                                final Function<GenderModel, Gender> mapGenderModelToDomain) {
        this.countryModelToDomain = countryModelToDomain;
        this.mapGenderModelToDomain = mapGenderModelToDomain;
    }

    @Override
    public Customer apply(final CustomerModel model) {
        if (model == null) return null;

        return Customer.from(
                CustomerId.from(model.getCustomerId()),
                FullName.from(model.getFirstName(), model.getLastName()),
                model.getBirthDate(),
                mapGenderModelToDomain.apply(model.getGender()),
                Address.from(
                        countryModelToDomain.apply(model.getCountry()),
                        model.getCity(),
                        PostalCode.from(model.getPostalCode()),
                        model.getStreetAddress()
                ),
                EmailAddress.from(model.getEmailAddress())
        );
    }

}
