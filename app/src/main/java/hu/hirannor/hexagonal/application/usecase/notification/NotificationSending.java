package hu.hirannor.hexagonal.application.usecase.notification;

public interface NotificationSending {
    void sentBySystem(SendSystemNotification command);
}
