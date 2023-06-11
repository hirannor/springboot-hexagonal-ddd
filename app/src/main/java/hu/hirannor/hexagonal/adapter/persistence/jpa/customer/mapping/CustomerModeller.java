package hu.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.*;
import hu.hirannor.hexagonal.domain.customer.*;
import hu.hirannor.hexagonal.infrastructure.modelling.Modeller;

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
    public static CustomerModeller applyChangesFrom(Customer domain) {
        return new CustomerModeller(domain);
    }

    @Override
    public CustomerModel to(final CustomerModel model) {
        model.setFirstName(domain.fullName().firstName());
        model.setLastName(domain.fullName().lastName());
        model.setGender(mapGenderToModel.apply(domain.gender()));
        model.setBirthDate(domain.birthDate());
        model.setCountry(mapCountryToModel.apply(domain.address().country()));
        model.setPostalCode(domain.address().postalCode().value());
        model.setCity(domain.address().city());
        model.setStreetAddress(domain.address().streetAddress());
        model.setEmailAddress(model.getEmailAddress());

        return model;

    }
}
