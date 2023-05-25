package com.hirannor.hexagonal.adapter.api.rest.mapping;

import com.hirannor.hexagonal.adapter.api.rest.model.AddressModel;
import com.hirannor.hexagonal.adapter.api.rest.model.CountryModel;
import com.hirannor.hexagonal.domain.customer.*;
import java.util.function.Function;

/**
 * Maps a {@link AddressModel} model type to {@link Address} domain type.
 *
 * @author Mate Karolyi
 */
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
