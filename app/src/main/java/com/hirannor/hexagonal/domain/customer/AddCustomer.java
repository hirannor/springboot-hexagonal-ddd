package com.hirannor.hexagonal.domain.customer;

import com.hirannor.hexagonal.infrastructure.command.Command;
import com.hirannor.hexagonal.infrastructure.command.CommandId;

import java.time.Instant;
import java.util.List;

public record AddCustomer(CommandId id,
                          Instant registeredAt,
                          FullName fullName,
                          Age age,
                          Gender gender,
                          List<Address> addresses,
                          EmailAddress emailAddress) implements Command {


    public static AddCustomer issue(final FullName fullName,
                                    final Age age,
                                    final Gender gender,
                                    final List<Address> addresses,
                                    final EmailAddress emailAddress) {

        return new AddCustomer(
                CommandId.generate(),
                Command.now(),
                fullName,
                age,
                gender,
                addresses,
                emailAddress
        );

    }

    public static AddCustomer recreate(final CommandId id,
                                       final Instant registeredAt,
                                       final FullName fullName,
                                       final Age age,
                                       final Gender gender,
                                       final List<Address> addresses,
                                       final EmailAddress emailAddress) {

        return new AddCustomer(
                id,
                registeredAt,
                fullName,
                age,
                gender,
                addresses,
                emailAddress
        );

    }
}
