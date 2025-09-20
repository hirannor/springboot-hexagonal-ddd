package io.github.hirannor.oms.domain.authentication;

import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

public record DoAuthenticate(CommandId id, EmailAddress emailAddress, Password password) implements Command {
    public static DoAuthenticate issue(final EmailAddress emailAddress, final Password password) {
        return new DoAuthenticate(CommandId.generate(), emailAddress, password);
    }
}
