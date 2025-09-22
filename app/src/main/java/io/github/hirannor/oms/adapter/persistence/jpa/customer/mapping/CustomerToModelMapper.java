package io.github.hirannor.oms.adapter.persistence.jpa.customer.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CountryModel;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.GenderModel;
import io.github.hirannor.oms.domain.customer.Address;
import io.github.hirannor.oms.domain.customer.Country;
import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.Gender;

import java.util.Optional;
import java.util.function.Consumer;
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

        model.setCustomerId(domain.id().asText());
        model.setGender(mapGenderToModel.apply(domain.gender()));
        model.setBirthDate(domain.birthDate());
        model.setFirstName(domain.firstName().value());
        model.setLastName(domain.lastName().value());
        Optional.ofNullable(domain.address())
                .filter(Address::isComplete)
                .ifPresent(applyAddressOn(model));

        model.setEmailAddress(domain.emailAddress().value());

        return model;
    }

    private Consumer<Address> applyAddressOn(CustomerModel model) {
        return addr -> {
            model.setCountry(mapCountryToModel.apply(addr.country()));
            model.setCity(addr.city());
            model.setPostalCode(addr.postalCode().value());
            model.setStreetAddress(addr.streetAddress());
        };
    }
}
