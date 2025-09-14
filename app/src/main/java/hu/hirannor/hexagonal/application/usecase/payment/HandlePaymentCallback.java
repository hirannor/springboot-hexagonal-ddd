package hu.hirannor.hexagonal.application.usecase.payment;

import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

public record HandlePaymentCallback(CommandId id, String payload, String signatureHeader) implements Command {

    public static HandlePaymentCallback issue(final String payload, final String signatureHeader) {
        return new HandlePaymentCallback(CommandId.generate(), payload, signatureHeader);
    }

}
