package com.hirannor.hexagonal.adapter.api.rest.mapping;


import com.hirannor.hexagonal.adapter.api.rest.model.AddressModel;
import com.hirannor.hexagonal.adapter.api.rest.model.CountryModel;
import com.hirannor.hexagonal.domain.customer.Address;
import com.hirannor.hexagonal.domain.customer.Country;

import java.util.function.Function;

class AddressToModelMapper implements Function<Address, AddressModel> {

    private final Function<Country, CountryModel> mapCountryToModel;

    AddressToModelMapper() {
        this(new CountryToModelMapper());
    }

    AddressToModelMapper(final Function<Country, CountryModel> mapCountryToModel) {
        this.mapCountryToModel = mapCountryToModel;
    }

    @Override
    public AddressModel apply(final Address domain) {
        final AddressModel model = new AddressModel();

        model.setCountry(mapCountryToModel.apply(domain.country()));
        model.setCity(domain.city());
        model.setStreetAddress(domain.streetAddress());
        model.setPostalCode(domain.postalCode().value());

        return model;

    }

}