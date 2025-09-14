package hu.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CountryModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.GenderModel;
import hu.hirannor.hexagonal.domain.customer.Country;
import hu.hirannor.hexagonal.domain.customer.Customer;
import hu.hirannor.hexagonal.domain.customer.Gender;

import java.util.function.Function;

/**
 * Maps a {@link Customer} domain notificationType to {@link CustomerModel} model notificationType.
 *
 * @author Mate Karolyi
 */
class CustomerToModelMapper implements Function<Customer, CustomerModel> {

    private final Function<Gender, GenderModel> mapGenderToModel;
    private final Function<Country, CountryModel> mapCountryToModel;

    CustomerToModelMapper() {
        this(
            new GenderToModelMapper(),
            new CountryToModelMapper()
        );
    }

    CustomerToModelMapper(
            final Function<Gender, GenderModel> mapGenderToModel,
            final Function<Country, CountryModel> mapCountryToModel) {

        this.mapGenderToModel = mapGenderToModel;
        this.mapCountryToModel = mapCountryToModel;
    }

    @Override
    public CustomerModel apply(final Customer domain) {
        if (domain == null) return null;

        final CustomerModel model = new CustomerModel();

        model.setCustomerId(domain.customerId().asText());
        model.setGender(mapGenderToModel.apply(domain.gender()));
        model.setBirthDate(domain.birthDate());
        model.setFirstName(domain.fullName().firstName());
        model.setLastName(domain.fullName().lastName());
        model.setCountry(mapCountryToModel.apply(domain.address().country()));
        model.setCity(domain.address().city());
        model.setPostalCode(domain.address().postalCode().value());
        model.setStreetAddress(domain.address().streetAddress());

        model.setEmailAddress(domain.emailAddress().value());

        return model;
    }

}
