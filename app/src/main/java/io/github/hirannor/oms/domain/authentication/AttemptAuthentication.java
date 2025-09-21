package io.github.hirannor.oms.domain.authentication;

import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

public record AttemptAuthentication(CommandId id, EmailAddress emailAddress, Password password) implements Command {
    public static AttemptAuthentication issue(final EmailAddress emailAddress, final Password password) {
        return new AttemptAuthentication(CommandId.generate(), emailAddress, password);
    }
}
