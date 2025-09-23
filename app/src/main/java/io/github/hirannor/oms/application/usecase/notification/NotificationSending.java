package io.github.hirannor.oms.application.usecase.notification;

public interface NotificationSending {
    void sendBySystem(SendSystemNotification command);
}
