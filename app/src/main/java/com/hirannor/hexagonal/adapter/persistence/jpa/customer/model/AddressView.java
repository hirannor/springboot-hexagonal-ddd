package com.hirannor.hexagonal.adapter.persistence.jpa.customer.model;

public interface AddressView {

    CountryModel getCountry();

    String getCity();

    Integer getPostalCode();

    String getStreetAddress();


}
