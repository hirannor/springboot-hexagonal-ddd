package io.github.hirannor.oms.application.port.notification;

public record SmsNotificationMessage(
        String recipient,
        String text
) implements NotificationMessage {

    @Override
    public String content() {
        return text;
    }

    public static SmsNotificationMessage create(String recipient, String text) {
        return new SmsNotificationMessage(recipient, text);
    }
}
