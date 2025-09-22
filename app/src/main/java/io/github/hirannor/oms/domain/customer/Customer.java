package io.github.hirannor.oms.domain.customer;

import io.github.hirannor.oms.domain.authentication.Register;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.command.ChangePersonalDetails;
import io.github.hirannor.oms.domain.customer.command.EnrollCustomer;
import io.github.hirannor.oms.domain.customer.event.CustomerRegistered;
import io.github.hirannor.oms.domain.customer.event.PersonalDetailsChanged;
import io.github.hirannor.oms.infrastructure.aggregate.AggregateRoot;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Domain specific customer object.
 *
 * @author Mate Karolyi
 */
public class Customer extends AggregateRoot {

    private final List<DomainEvent> events;
    private final CustomerId customerId;
    private FirstName firstName;
    private LastName lastName;
    private LocalDate birthDate;
    private Gender gender;
    private Address address;
    private EmailAddress emailAddress;

    Customer(final CustomerId customerId,
             final FirstName firstName,
             final LastName lastName,
             final LocalDate birthDate,
             final Gender gender,
             final Address address,
             final EmailAddress emailAddress) {

        Objects.requireNonNull(customerId);
        Objects.requireNonNull(emailAddress);

        this.events = new ArrayList<>(0);
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.emailAddress = emailAddress;
    }

    public static Customer from(
            final CustomerId customerId,
            final FirstName firstName,
            final LastName lastName,
            final LocalDate birthDate,
            final Gender gender,
            final Address address,
            final EmailAddress emailAddress) {

        return CustomerBuilder.empty()
                .customerId(customerId)
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .gender(gender)
                .address(address)
                .emailAddress(emailAddress)
                .createCustomer();
    }

    /**
     * Registers a customer by a command.
     *
     * @param cmd {@link EnrollCustomer} command
     * @return an instance of newly created {@link Customer} object
     */
    public static Customer registerBy(final Register cmd) {
        if (cmd == null) throw new IllegalArgumentException("AddCustomer command cannot be null!");

        final CustomerId customerId = CustomerId.generate();

        final Customer newCustomer = CustomerBuilder.empty()
                .customerId(customerId)
                .emailAddress(cmd.emailAddress())
                .createCustomer();

        newCustomer.events.add(CustomerRegistered.issue(customerId));

        return newCustomer;
    }

    /**
     * Change customer details based on the command
     *
     * @param cmd {@link ChangePersonalDetails}
     * @return a modified instance of {@link Customer}
     */
    public Customer changePersonalDetailsBy(final ChangePersonalDetails cmd) {
        Optional.ofNullable(cmd.firstName()).ifPresent(this::changeFirstName);
        Optional.ofNullable(cmd.lastName()).ifPresent(this::changeLastName);
        Optional.ofNullable(cmd.birthDate()).ifPresent(this::changeBirthDate);
        Optional.ofNullable(cmd.gender()).ifPresent(this::changeGender);
        Optional.ofNullable(cmd.address()).ifPresent(this::updateAddress);

        this.events.add(PersonalDetailsChanged.issue(customerId));

        return this;
    }

    public boolean hasCompleteAddress() {
        return Optional.ofNullable(address)
                .map(Address::isComplete)
                .orElse(false);
    }

    public CustomerId id() {
        return customerId;
    }

    public FirstName firstName() {
        return firstName;
    }

    public LastName lastName() {
        return lastName;
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

    public String fullName() {
        return this.firstName.asText() + " " + this.lastName.asText();
    }

    @Override
    public void clearEvents() {
        events.clear();
    }

    @Override
    public List<DomainEvent> events() {
        return List.copyOf(events);
    }

    private void changeFirstName(final FirstName firstName) {
        this.firstName = firstName;
    }

    private void changeLastName(final LastName lastName) {
        this.lastName = lastName;
    }

    private void changeGender(final Gender gender) {
        this.gender = gender;
    }

    private void changeBirthDate(final LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    private void updateAddress(final Address address) {
        this.address = address;
    }

}
