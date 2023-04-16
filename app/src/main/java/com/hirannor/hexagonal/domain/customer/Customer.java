package com.hirannor.hexagonal.domain.customer;

import com.hirannor.hexagonal.infrastructure.aggregate.AggregateRoot;
import com.hirannor.hexagonal.infrastructure.event.DomainEvent;

import java.util.ArrayList;
import java.util.List;

public class Customer implements AggregateRoot {

    private final List<DomainEvent> events;
    private final CustomerId customerId;
    private final FullName fullName;
    private final Age age;
    private final Gender gender;
    private final List<Address> addresses;
    private final EmailAddress emailAddress;

    private Customer(final CustomerId customerId,
                     final FullName fullName,
                     final Age age,
                     final Gender gender,
                     final List<Address> addresses,
                     final EmailAddress emailAddress) {

        this.events = new ArrayList<>(0);
        this.customerId = customerId;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.addresses = addresses;
        this.emailAddress = emailAddress;
    }

    public static Customer from(
            final CustomerId customerId,
            final FullName fullName,
            final Age age,
            final Gender gender,
            final List<Address> addresses,
            final EmailAddress emailAddress) {

        return new Customer(customerId, fullName, age, gender, addresses, emailAddress);
    }

    public static Customer register(final AddCustomer cmd) {
        if (cmd == null) throw new IllegalArgumentException("AddCustomer command cannot be null!");

        final CustomerId customerId = CustomerId.generate();

        final Customer newCustomer = new Customer(
                customerId,
                cmd.fullName(),
                cmd.age(),
                cmd.gender(),
                cmd.addresses(),
                cmd.emailAddress()
        );

        newCustomer.events.add(CustomerAdded.issue(customerId));

        return newCustomer;
    }


    public CustomerId customerId() {
        return customerId;
    }

    public FullName fullName() {
        return fullName;
    }

    public Age age() {
        return age;
    }

    public Gender gender() {
        return gender;
    }

    public List<Address> addresses() {
        return addresses;
    }

    public EmailAddress emailAddress() {
        return emailAddress;
    }

    @Override
    public void clearEvents() {
        events.clear();
    }

    @Override
    public List<DomainEvent> listEvents() {
        return List.copyOf(events);
    }
}
