package com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.*;
import com.hirannor.hexagonal.domain.customer.*;
import com.hirannor.hexagonal.infrastructure.modelling.Modeller;
import java.util.function.Function;

public class CustomerModeller implements Modeller<CustomerModel> {

    private final Function<Country, CountryModel> mapCountryToModel;
    private final Function<Gender, GenderModel> mapGenderToModel;

    private final Customer domain;

    CustomerModeller(final Customer domain) {
        mapCountryToModel = new CountryToModelMapper();
        mapGenderToModel = new GenderToModelMapper();

        this.domain = domain;
    }

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
