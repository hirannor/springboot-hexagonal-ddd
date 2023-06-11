package hu.hirannor.hexagonal.adapter.web.rest.customer.mapping;


import hu.hirannor.hexagonal.adapter.web.rest.model.AddressModel;
import hu.hirannor.hexagonal.adapter.web.rest.model.CountryModel;
import hu.hirannor.hexagonal.domain.customer.Address;
import hu.hirannor.hexagonal.domain.customer.Country;
import java.util.function.Function;

/**
 * Maps a {@link Address} domain type to {@link AddressModel} model type.
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
        final AddressModel model = new AddressModel();

        model.setCountry(mapCountryToModel.apply(domain.country()));
        model.setCity(domain.city());
        model.setStreetAddress(domain.streetAddress());
        model.setPostalCode(domain.postalCode().value());

        return model;

    }

}