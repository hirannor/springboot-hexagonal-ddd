package com.hirannor.hexagonal.domain.customer;

import com.hirannor.hexagonal.infrastructure.command.Command;
import com.hirannor.hexagonal.infrastructure.command.CommandId;

import java.time.Instant;
import java.time.LocalDate;

public record RegisterCustomer(CommandId id,
                               Instant registeredAt,
                               FullName fullName,
                               LocalDate birthDate,
                               Gender gender,
                               Address address,
                               EmailAddress emailAddress) implements Command {


    public static RegisterCustomer issue(final FullName fullName,
                                         final LocalDate birthDate,
                                         final Gender gender,
                                         final Address address,
                                         final EmailAddress emailAddress) {
        return new RegisterCustomer(
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
