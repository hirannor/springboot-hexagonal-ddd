package hu.hirannor.hexagonal.application.port.notification;

public interface NotificationMessage {
    String recipient();
    String content();
}
