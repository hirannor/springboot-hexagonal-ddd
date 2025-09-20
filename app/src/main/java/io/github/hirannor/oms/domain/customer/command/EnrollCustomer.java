package io.github.hirannor.oms.domain.customer.command;

import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Address;
import io.github.hirannor.oms.domain.customer.FullName;
import io.github.hirannor.oms.domain.customer.Gender;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Immutable record that represents a command to enroll a new customer.
 *
 * @param id           {@link CommandId} unique identifier of command operation
 * @param registeredAt {@link Instant} registration time of command
 * @param fullName     {@link FullName} full name of customer
 * @param birthDate    {@link LocalDate} birth date of customer
 * @param gender       {@link Gender} gender of customer
 * @param address      {@link Address} emailAddress of customer
 * @param emailAddress {@link EmailAddress} email emailAddress of customer
 * @author Mate Karolyi
 */
public record EnrollCustomer(CommandId id,
                             Instant registeredAt,
                             FullName fullName,
                             LocalDate birthDate,
                             Gender gender,
                             Address address,
                             EmailAddress emailAddress) implements Command {


    /**
     * Issues a {@link EnrollCustomer} command.
     *
     * @param fullName
     * @param birthDate
     * @param gender
     * @param address
     * @param emailAddress
     * @return an instance of {@link EnrollCustomer} command
     */
    public static EnrollCustomer issue(final FullName fullName,
                                       final LocalDate birthDate,
                                       final Gender gender,
                                       final Address address,
                                       final EmailAddress emailAddress) {
        return new EnrollCustomer(
                CommandId.generate(),
                Command.now(),
                fullName,
                birthDate,
                gender,
                address,
                emailAddress
        );
    }
}
