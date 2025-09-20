package io.github.hirannor.oms.adapter.persistence.jpa.customer.model;

import java.time.LocalDate;

/**
 * A view representation of a Customer used for projection.
 *
 * @author Mate Karolyi
 */
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
