package com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.AddressView;
import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CountryModel;
import com.hirannor.hexagonal.domain.customer.Address;
import com.hirannor.hexagonal.domain.customer.Country;
import com.hirannor.hexagonal.domain.customer.PostalCode;

import java.util.function.Function;

public class AddressViewToDomainMapper implements Function<AddressView, Address> {

    private final Function<CountryModel, Country> mapModelToDomain;

    AddressViewToDomainMapper() {
        this(new CountryModelToDomainMapper());
    }

    AddressViewToDomainMapper(final Function<CountryModel, Country> mapModelToDomain) {
        this.mapModelToDomain = mapModelToDomain;
    }

    @Override
    public Address apply(final AddressView view) {
        if (view == null) return null;

        return Address.from(
                mapModelToDomain.apply(view.getCountry()),
                view.getCity(),
                PostalCode.from(view.getPostalCode()),
                view.getStreetAddress()
        );
    }

}
