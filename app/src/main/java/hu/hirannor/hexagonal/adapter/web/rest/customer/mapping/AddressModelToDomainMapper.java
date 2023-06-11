package hu.hirannor.hexagonal.adapter.web.rest.customer.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.customer.model.AddressModel;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.CountryModel;
import hu.hirannor.hexagonal.domain.customer.*;

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
