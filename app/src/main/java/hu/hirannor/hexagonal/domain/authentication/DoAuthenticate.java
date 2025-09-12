package hu.hirannor.hexagonal.domain.authentication;

import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

public record DoAuthenticate(CommandId id, EmailAddress emailAddress, Password password) implements Command {
    public static DoAuthenticate issue(final EmailAddress emailAddress, final Password password) {
        return new DoAuthenticate(CommandId.generate(), emailAddress, password);
    }
}
