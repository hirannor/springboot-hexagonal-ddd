package io.github.hirannor.oms.domain.customer.command;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Address;
import io.github.hirannor.oms.domain.customer.FirstName;
import io.github.hirannor.oms.domain.customer.Gender;
import io.github.hirannor.oms.domain.customer.LastName;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Immutable record that represents a command to hold changed customer details.
 *
 * @param id           {@link CommandId} unique identifier of command operation
 * @param registeredAt {@link Instant} registration time of command
 * @param customerId   {@link CustomerId} unique identifier of a customer
 * @param firstName    {@link FirstName} first name of customer
 * @param lastName     {@link LastName} last name of customer
 * @param birthDate    {@link LocalDate} birth date of customer
 * @param gender       {@link Gender} gender of customer
 * @param address      {@link Address} emailAddress of customer
 * @param email        {@link EmailAddress} email emailAddress of customer
 * @author Mate Karolyi
 */
public record ChangePersonalDetails(
        CommandId id,
        Instant registeredAt,
        CustomerId customerId,
        FirstName firstName,
        LastName lastName,
        LocalDate birthDate,
        Gender gender,
        Address address,
        EmailAddress email) implements Command {

    public static class Builder {
        private CustomerId customerId;
        private FirstName firstName;
        private LastName lastName;
        private LocalDate birthDate;
        private Gender gender;
        private Address address;
        private EmailAddress email;

        public Builder customerId(final CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder firstName(final FirstName firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(final LastName lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder birthDate(final LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder gender(final Gender gender) {
            this.gender = gender;
            return this;
        }


        public Builder address(final Address address) {
            this.address = address;
            return this;
        }

        public Builder email(final EmailAddress email) {
            this.email = email;
            return this;
        }

        public ChangePersonalDetails assemble() {
            return new ChangePersonalDetails(
                    CommandId.generate(),
                    Command.now(),
                    customerId,
                    firstName,
                    lastName,
                    birthDate,
                    gender,
                    address,
                    email
            );
        }
    }
}
