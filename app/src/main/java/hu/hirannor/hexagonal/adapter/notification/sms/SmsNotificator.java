package hu.hirannor.hexagonal.adapter.notification.sms;

import hu.hirannor.hexagonal.application.port.notification.Notificator;
import hu.hirannor.hexagonal.application.port.notification.SendNotification;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@DrivenAdapter
public class SmsNotificator implements Notificator {

    private static final Logger LOGGER = LogManager.getLogger(
        SmsNotificator.class
    );

    SmsNotificator() {}

    @Override
    public void send(final SendNotification notification) {

    }
}
