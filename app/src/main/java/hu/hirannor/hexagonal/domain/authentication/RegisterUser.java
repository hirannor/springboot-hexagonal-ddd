package hu.hirannor.hexagonal.domain.authentication;

import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

public record RegisterUser(CommandId id, EmailAddress address, String password) implements Command {

    public static RegisterUser issue(final EmailAddress email, final String password) {
        return new RegisterUser(CommandId.generate(), email, password);
    }
}
