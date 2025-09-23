package io.github.hirannor.oms.application.port.notification;

import io.github.hirannor.oms.application.port.notification.data.NotificationData;

public interface NotificationFactory {
    NotificationMessage createNotification(final NotificationData data);
}