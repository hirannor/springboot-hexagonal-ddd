package hu.hirannor.hexagonal.domain.authentication;

import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

public record Register(CommandId id, EmailAddress emailAddress, Password password) implements Command {

    public static Register issue(final EmailAddress email, final Password password) {
        return new Register(CommandId.generate(), email, password);
    }
}
