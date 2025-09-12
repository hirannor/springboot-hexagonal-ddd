package hu.hirannor.hexagonal.adapter.notification.email;

import hu.hirannor.hexagonal.application.port.notification.Notificator;
import hu.hirannor.hexagonal.application.port.notification.SendEmailNotification;
import hu.hirannor.hexagonal.application.port.notification.SendNotification;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@DrivenAdapter
class EmailNotificator implements Notificator {

    private static final Logger LOGGER = LogManager.getLogger(
        EmailNotificator.class
    );

    EmailNotificator() {}

    @Override
    public void send(final SendNotification notification) {
        if (!(notification instanceof SendEmailNotification emailNotification)) {
            throw new IllegalArgumentException("Invalid notification type");
        }
    }
}
