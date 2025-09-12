package hu.hirannor.hexagonal.application.port.notification;

public interface Notificator {
    void send(SendNotification notification);
}
