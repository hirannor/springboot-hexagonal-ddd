package io.github.hirannor.oms.adapter.notification.sms;

import io.github.hirannor.oms.application.port.notification.NotificationMessage;
import io.github.hirannor.oms.application.port.notification.Notificator;
import io.github.hirannor.oms.application.port.notification.SmsNotificationMessage;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@DrivenAdapter
public class SmsNotificator implements Notificator {

    private static final Logger LOGGER = LogManager.getLogger(
            SmsNotificator.class
    );

    SmsNotificator() {
    }

    @Override
    public void send(final NotificationMessage notification) {
        if (!(notification instanceof SmsNotificationMessage message)) {
            throw new IllegalArgumentException("Invalid notification notificationType");
        }
    }
}
