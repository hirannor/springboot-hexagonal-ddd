package io.github.hirannor.oms.adapter.persistence.jpa.customer.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CountryModel;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerView;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.GenderModel;

import java.time.LocalDate;

public record CustomerViewRecord(String customerId,
                                 String firstName,
                                 String lastName,
                                 LocalDate birthDate,
                                 GenderModel gender,
                                 CountryModel country,
                                 Integer postalCode,
                                 String city,
                                 String streetAddress,
                                 String emailAddress) implements CustomerView {
    @Override
    public String getCustomerId() {
        return this.customerId;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    @Override
    public GenderModel getGender() {
        return this.gender;
    }

    @Override
    public CountryModel getCountry() {
        return this.country;
    }

    @Override
    public String getCity() {
        return this.city;
    }

    @Override
    public Integer getPostalCode() {
        return this.postalCode;
    }

    @Override
    public String getStreetAddress() {
        return this.streetAddress;
    }

    @Override
    public String getEmailAddress() {
        return this.emailAddress;
    }
}

