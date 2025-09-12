package hu.hirannor.hexagonal.domain.customer;

import hu.hirannor.hexagonal.domain.EmailAddress;

import java.time.LocalDate;

public final class CustomerBuilder {
    private CustomerId customerId;
    private FullName fullName;
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

    public CustomerBuilder fullName(final FullName fullName) {
        this.fullName = fullName;
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
        return new Customer(customerId, fullName, birthDate, gender, address, emailAddress);
    }
}