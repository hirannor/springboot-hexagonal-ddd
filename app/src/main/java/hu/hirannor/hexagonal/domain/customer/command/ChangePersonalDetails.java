package hu.hirannor.hexagonal.domain.customer.command;

import hu.hirannor.hexagonal.domain.customer.*;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Immutable record that represents a command to hold changed customer details.
 *
 * @param id           {@link CommandId} unique identifier of command operation
 * @param registeredAt {@link Instant} registration time of command
 * @param customerId   {@link CustomerId} unique identifier of a customer
 * @param fullName     {@link FullName} full name of customer
 * @param birthDate    {@link LocalDate} birth date of customer
 * @param gender       {@link Gender} gender of customer
 * @param address      {@link Address} address of customer
 * @param email        {@link EmailAddress} email address of customer
 * @author Mate Karolyi
 */
public record ChangePersonalDetails(
        CommandId id,
        Instant registeredAt,
        CustomerId customerId,
        FullName fullName,
        LocalDate birthDate,
        Gender gender,
        Address address,
        EmailAddress email) implements Command {

    public static class Builder {
        private CustomerId customerId;
        private FullName fullName;
        private LocalDate birthDate;
        private Gender gender;
        private Address address;
        private EmailAddress email;

        public Builder customerId(final CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder fullName(final FullName fullName) {
            this.fullName = fullName;
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
                    fullName,
                    birthDate,
                    gender,
                    address,
                    email
            );
        }
    }
}
