package io.github.hirannor.oms.domain.customer;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;

import java.time.LocalDate;

public final class CustomerBuilder {
    private CustomerId customerId;
    private FirstName firstName;
    private LastName lastName;
    private LocalDate birthDate;
    private Gender gender;
    private Address address;
    private EmailAddress emailAddress;

    public static CustomerBuilder empty() {
        return new CustomerBuilder();
    }

    public CustomerBuilder customerId(final CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public CustomerBuilder firstName(final FirstName firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerBuilder lastName(final LastName lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerBuilder birthDate(final LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public CustomerBuilder gender(final Gender gender) {
        this.gender = gender;
        return this;
    }

    public CustomerBuilder address(final Address address) {
        this.address = address;
        return this;
    }

    public CustomerBuilder emailAddress(final EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public Customer createCustomer() {
        return new Customer(customerId, firstName, lastName, birthDate, gender, address, emailAddress);
    }
}