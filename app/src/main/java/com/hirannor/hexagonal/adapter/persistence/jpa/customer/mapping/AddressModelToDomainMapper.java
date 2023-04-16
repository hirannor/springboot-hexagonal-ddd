package com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.AddressModel;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CountryModel;
import com.hirannor.hexagonal.domain.customer.Address;
import com.hirannor.hexagonal.domain.customer.Country;
import com.hirannor.hexagonal.domain.customer.PostalCode;

import java.util.function.Function;

class AddressModelToDomainMapper implements Function<AddressModel, Address> {

    private final Function<CountryModel, Country> mapModelToDomain;

    AddressModelToDomainMapper() {
        this(new CountryModelToDomainMapper());
    }

    AddressModelToDomainMapper(final Function<CountryModel, Country> mapModelToDomain) {
        this.mapModelToDomain = mapModelToDomain;
    }

    @Override
    public Address apply(final AddressModel model) {
        if (model == null) return null;

        return Address.from(
                mapModelToDomain.apply(model.getCountry()),
                model.getCity(),
                PostalCode.from(model.getPostalCode()),
                model.getStreetAddress()
        );
    }

}
