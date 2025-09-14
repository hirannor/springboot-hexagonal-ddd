package hu.hirannor.hexagonal.application.port.notification;

public interface NotificationFactory {
    NotificationMessage createNotification(final NotificationData data);
}