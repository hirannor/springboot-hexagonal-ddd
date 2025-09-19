package hu.hirannor.hexagonal.adapter.web.rest.customer.mapping;


import hu.hirannor.hexagonal.adapter.web.rest.customer.model.AddressModel;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.CountryModel;
import hu.hirannor.hexagonal.domain.customer.Address;
import hu.hirannor.hexagonal.domain.customer.Country;
import java.util.function.Function;

/**
 * Maps a {@link Address} domain notificationType to {@link AddressModel} model notificationType.
 *
 * @author Mate Karolyi
 */
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
        if (domain == null) return null;

        final AddressModel model = new AddressModel();

        model.setCountry(mapCountryToModel.apply(domain.country()));
        model.setCity(domain.city());
        model.setStreetAddress(domain.streetAddress());
        model.setPostalCode(domain.postalCode().value());

        return model;

    }

}