package io.github.hirannor.oms.application.port.notification;

import java.util.Map;

public record EmailNotificationMessage(
        String recipient,
        String subject,
        String templateName,
        Map<String, Object> variables
) implements NotificationMessage {

    @Override
    public String content() {
        return subject;
    }

    public static EmailNotificationMessage create(
            final String recipient,
            final String subject,
            final String templateName,
            final Map<String, Object> variables
    ) {
        return new EmailNotificationMessage(recipient, subject, templateName, variables);
    }
}
