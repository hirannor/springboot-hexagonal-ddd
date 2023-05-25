package com.hirannor.hexagonal.adapter.persistence.jpa.customer.model;

import java.time.LocalDate;

public interface CustomerView {

    String getCustomerId();

    String getFirstName();

    String getLastName();

    LocalDate getBirthDate();

    GenderModel getGender();

    CountryModel getCountry();

    String getCity();

    Integer getPostalCode();

    String getStreetAddress();

    String getEmailAddress();

}
