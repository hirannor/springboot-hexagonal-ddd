package hu.hirannor.hexagonal.application.port.notification;

import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

import java.util.List;

public record SendEmailNotification(CommandId id,
                                    EmailAddress from,
                                    List<EmailAddress> recipients) implements SendNotification, Command {
    public static SendEmailNotification issue(final EmailAddress from, final List<EmailAddress> recipients) {
        return new SendEmailNotification(CommandId.generate(), from, recipients);
    }
}
