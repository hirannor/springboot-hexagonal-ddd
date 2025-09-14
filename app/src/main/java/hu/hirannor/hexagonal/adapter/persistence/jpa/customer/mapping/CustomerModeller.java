package hu.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CountryModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.GenderModel;
import hu.hirannor.hexagonal.domain.customer.*;
import hu.hirannor.hexagonal.infrastructure.modelling.Modeller;

import java.util.Optional;
import java.util.function.Function;

/**
 * An implementation of {@link Modeller} interface, which is capable of
 * applying changes from a {@link Customer} domain type to a {@link CustomerModel} model type.
 *
 * @author Mate Karolyi
 */
public class CustomerModeller implements Modeller<CustomerModel> {

    private final Function<Country, CountryModel> mapCountryToModel;
    private final Function<Gender, GenderModel> mapGenderToModel;

    private final Customer domain;

    CustomerModeller(final Customer domain) {
        mapCountryToModel = new CountryToModelMapper();
        mapGenderToModel = new GenderToModelMapper();

        this.domain = domain;
    }

    /**
     * Create an instance of {@link CustomerModeller}.
     *
     * @param domain {@link Customer} from which the changes should be applied
     * @return an instance of {@link CustomerModeller}
     */
    public static CustomerModeller applyChangesFrom(final Customer domain) {
        return new CustomerModeller(domain);
    }

    @Override
    public CustomerModel to(final CustomerModel model) {
        if (model == null) return null;

        model.setCustomerId(domain.customerId().asText());

        Optional.ofNullable(domain.fullName())
                .map(FullName::firstName)
                .ifPresent(model::setFirstName);
        Optional.ofNullable(domain.fullName())
                .map(FullName::lastName)
                .ifPresent(model::setLastName);

        Optional.ofNullable(domain.gender())
                .map(mapGenderToModel)
                .ifPresent(model::setGender);

        Optional.ofNullable(domain.birthDate())
                .ifPresent(model::setBirthDate);

        Optional.ofNullable(domain.address())
                .map(mapToCountryModel())
                .ifPresent(model::setCountry);

        Optional.ofNullable(domain.address())
                .map(mapToPostalCode())
                .ifPresent(model::setPostalCode);

        Optional.ofNullable(domain.address())
                .map(Address::city)
                .ifPresent(model::setCity);

        Optional.ofNullable(domain.address())
                .map(Address::streetAddress)
                .ifPresent(model::setStreetAddress);

        model.setEmailAddress(domain.emailAddress().value());

        return model;
    }

    private Function<Address, CountryModel> mapToCountryModel() {
        final Function<Address, Country> extractCountry = Address::country;
        return extractCountry.andThen(mapCountryToModel);
    }

    private Function<Address, Integer> mapToPostalCode() {
        Function<Address, PostalCode> extractPostalCode = Address::postalCode;
        return extractPostalCode.andThen(PostalCode::value);
    }
}
