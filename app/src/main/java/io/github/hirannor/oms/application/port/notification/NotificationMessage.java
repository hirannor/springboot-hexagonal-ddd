package io.github.hirannor.oms.application.port.notification;

public interface NotificationMessage {
    String recipient();

    String content();
}
