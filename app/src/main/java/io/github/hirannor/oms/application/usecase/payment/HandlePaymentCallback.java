package io.github.hirannor.oms.application.usecase.payment;

import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

public record HandlePaymentCallback(CommandId id, String payload, String signatureHeader) implements Command {

    public static HandlePaymentCallback issue(final String payload, final String signatureHeader) {
        return new HandlePaymentCallback(CommandId.generate(), payload, signatureHeader);
    }

}
