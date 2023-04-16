package com.hirannor.hexagonal.adapter.persistence.jpa.customer.model;

import java.util.List;

public interface CustomerView {

    String getCustomerId();

    String getFirstName();

    String getLastName();

    Integer getAge();

    GenderModel getGender();

    List<AddressView> getAddresses();

    String getEmailAddress();

}
