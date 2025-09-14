package hu.hirannor.hexagonal.adapter.web.rest.customer.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.customer.model.AddressModel;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.CountryModel;
import hu.hirannor.hexagonal.domain.customer.Address;
import hu.hirannor.hexagonal.domain.customer.Country;
import hu.hirannor.hexagonal.domain.customer.PostalCode;

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
