package io.github.hirannor.oms.adapter.persistence.jpa.customer.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CountryModel;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.GenderModel;
import io.github.hirannor.oms.domain.customer.*;
import io.github.hirannor.oms.infrastructure.modelling.Modeller;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An implementation of {@link Modeller} interface, which is capable of
 * applying changes from a {@link Customer} domain notificationType to a {@link CustomerModel} model notificationType.
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

        model.setCustomerId(domain.id().asText());

        Optional.ofNullable(domain.firstName())
                .map(FirstName::value)
                .ifPresent(model::setFirstName);
        Optional.ofNullable(domain.lastName())
                .map(LastName::value)
                .ifPresent(model::setLastName);

        Optional.ofNullable(domain.gender())
                .map(mapGenderToModel)
                .ifPresent(model::setGender);

        Optional.ofNullable(domain.birthDate())
                .ifPresent(model::setBirthDate);

        Optional.ofNullable(domain.address())
                .filter(Address::isComplete)
                .ifPresent(updateAddressOn(model));

        model.setEmailAddress(domain.emailAddress().value());

        return model;
    }

    private Consumer<Address> updateAddressOn(CustomerModel model) {
        return addr -> {
            model.setCountry(mapCountryToModel.apply(addr.country()));
            model.setPostalCode(addr.postalCode().value());
            model.setCity(addr.city());
            model.setStreetAddress(addr.streetAddress());
        };
    }
}
