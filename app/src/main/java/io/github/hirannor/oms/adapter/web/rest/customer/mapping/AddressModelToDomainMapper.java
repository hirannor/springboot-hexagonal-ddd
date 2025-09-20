package io.github.hirannor.oms.adapter.web.rest.customer.mapping;

import io.github.hirannor.oms.adapter.web.rest.customer.model.AddressModel;
import io.github.hirannor.oms.adapter.web.rest.customer.model.CountryModel;
import io.github.hirannor.oms.domain.customer.Address;
import io.github.hirannor.oms.domain.customer.Country;
import io.github.hirannor.oms.domain.customer.PostalCode;

import java.util.function.Function;

/**
 * Maps a {@link AddressModel} model notificationType to {@link Address} domain notificationType.
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
