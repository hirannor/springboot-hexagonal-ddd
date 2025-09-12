package hu.hirannor.hexagonal.domain.authentication;

import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

public record AuthenticateUser(CommandId id, EmailAddress emailAddress, Password password) implements Command {
    public static AuthenticateUser issue(final EmailAddress emailAddress, final Password password) {
        return new AuthenticateUser(CommandId.generate(), emailAddress, password);
    }
}
