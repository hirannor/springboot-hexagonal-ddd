package io.github.hirannor.oms.application.port.notification;

public interface NotificationFactory {
    NotificationMessage createNotification(final NotificationData data);
}