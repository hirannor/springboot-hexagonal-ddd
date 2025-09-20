package io.github.hirannor.oms.domain.authentication;

import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

public record Register(CommandId id, EmailAddress emailAddress, Password password) implements Command {

    public static Register issue(final EmailAddress email, final Password password) {
        return new Register(CommandId.generate(), email, password);
    }
}
