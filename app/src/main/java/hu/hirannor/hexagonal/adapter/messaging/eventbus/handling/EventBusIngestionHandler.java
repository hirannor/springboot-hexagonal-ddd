package hu.hirannor.hexagonal.adapter.messaging.eventbus.handling;

import hu.hirannor.hexagonal.domain.customer.event.PersonalDetailsChanged;
import hu.hirannor.hexagonal.domain.customer.event.CustomerRegistered;
import hu.hirannor.hexagonal.infrastructure.adapter.DriverAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Event bus ingestion handler implementation to handle various events such as: domain, application events.
 *
 * @author Mate Karolyi
 */
@Component
@DriverAdapter
class EventBusIngestionHandler {

    private static final Logger LOGGER = LogManager.getLogger(
            EventBusIngestionHandler.class
    );

    EventBusIngestionHandler() {
    }

    /**
     * Handles incoming {@link CustomerRegistered} event.
     *
     * @param evt {@link CustomerRegistered} to be handled
     */
    @Async
    @TransactionalEventListener
    public void handle(final CustomerRegistered evt) {
        if (evt == null) throw new IllegalArgumentException("Customer registered event cannot be null!");

        LOGGER.debug("CustomerRegistered event received: {}", evt);
    }

    /**
     * Handles incoming {@link PersonalDetailsChanged} event.
     *
     * @param evt {@link PersonalDetailsChanged} to be handled
     */
    @Async
    @TransactionalEventListener
    public void handle(final PersonalDetailsChanged evt) {
        if (evt == null) throw new IllegalArgumentException("Customer details changed event cannot be null!");

        LOGGER.debug("CustomerDetailsChanged event received: {}", evt);
    }

}