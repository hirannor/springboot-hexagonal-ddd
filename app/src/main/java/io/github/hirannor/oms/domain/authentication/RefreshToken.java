package io.github.hirannor.oms.domain.authentication;

import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

import java.util.Objects;

public record RefreshToken(CommandId id, String refreshToken) implements Command {

    public RefreshToken {
        Objects.requireNonNull(refreshToken, "refreshToken must not be null");
    }

    public static RefreshToken issue(final String refreshToken) {
        return new RefreshToken(CommandId.generate(), refreshToken);
    }
}
