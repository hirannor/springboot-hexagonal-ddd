package com.hirannor.hexagonal.domain.customer;

import com.hirannor.hexagonal.infrastructure.aggregate.AggregateRoot;
import com.hirannor.hexagonal.infrastructure.event.DomainEvent;
import java.time.LocalDate;
import java.util.*;

/**
 * Domain specific customer object.
 *
 * @author Mate Karolyi
 */
public class Customer implements AggregateRoot {

    private final List<DomainEvent> events;
    private final CustomerId customerId;
    private FullName fullName;
    private LocalDate birthDate;
    private Gender gender;
    private Address address;
    private EmailAddress emailAddress;

    private Customer(final CustomerId customerId,
                     final FullName fullName,
                     final LocalDate birthDate,
                     final Gender gender,
                     final Address address,
                     final EmailAddress emailAddress) {

        Objects.requireNonNull(customerId);
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(birthDate);
        Objects.requireNonNull(gender);
        Objects.requireNonNull(address);
        Objects.requireNonNull(emailAddress);

        this.events = new ArrayList<>(0);
        this.customerId = customerId;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.emailAddress = emailAddress;
    }

    public static Customer from(
        final CustomerId customerId,
        final FullName fullName,
        final LocalDate birthDate,
        final Gender gender,
        final Address address,
        final EmailAddress emailAddress) {

        return new Customer(customerId, fullName, birthDate, gender, address, emailAddress);
    }

    /**
     * Registers a customer by a command.
     *
     * @param cmd {@link RegisterCustomer} command
     * @return an instance of newly created {@link Customer} object
     */
    public static Customer registerBy(final RegisterCustomer cmd) {
        if (cmd == null) throw new IllegalArgumentException("AddCustomer command cannot be null!");

        final CustomerId customerId = CustomerId.generate();

        final Customer newCustomer = new Customer(
            customerId,
            cmd.fullName(),
            cmd.birthDate(),
            cmd.gender(),
            cmd.address(),
            cmd.emailAddress()
        );

        newCustomer.events.add(CustomerRegistered.issue(customerId));

        return newCustomer;
    }

    /**
     * Change customer details based on the command
     *
     * @param cmd {@link ChangeCustomerDetails}
     * @return a modified instance of {@link Customer}
     */
    public Customer changeDetailsBy(final ChangeCustomerDetails cmd) {
        this.fullName = cmd.fullName();
        this.birthDate = cmd.birthDate();
        this.address = cmd.address();
        this.gender = cmd.gender();
        this.emailAddress = cmd.email();

        this.events.add(CustomerDetailsChanged.issue(customerId));

        return this;
    }


    public CustomerId customerId() {
        return customerId;
    }

    public FullName fullName() {
        return fullName;
    }

    public LocalDate birthDate() {
        return birthDate;
    }

    public Gender gender() {
        return gender;
    }

    public Address address() {
        return address;
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
