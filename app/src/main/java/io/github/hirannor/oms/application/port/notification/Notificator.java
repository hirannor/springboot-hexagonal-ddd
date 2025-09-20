package io.github.hirannor.oms.application.port.notification;

public interface Notificator {
    void send(NotificationMessage notification);
}
