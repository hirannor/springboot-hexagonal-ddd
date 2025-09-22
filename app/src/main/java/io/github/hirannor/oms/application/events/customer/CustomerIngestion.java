package io.github.hirannor.oms.application.events.customer;

import io.github.hirannor.oms.domain.customer.event.CustomerRegistered;
import io.github.hirannor.oms.domain.customer.event.PersonalDetailsChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class CustomerIngestion {
    private static final Logger LOGGER = LogManager.getLogger(
            CustomerIngestion.class
    );

    CustomerIngestion() {
    }

    /**
     * Handles incoming {@link CustomerRegistered} event.
     *
     * @param evt {@link CustomerRegistered} to be handled
     */
    @EventListener
    public void handle(final CustomerRegistered evt) {
        if (evt == null) throw new IllegalArgumentException("Customer registered event cannot be null!");

        LOGGER.debug("CustomerRegistered event received: {}", evt);
    }

    @EventListener
    public void handle(final PersonalDetailsChanged evt) {
        if (evt == null) throw new IllegalArgumentException("Customer details changed event cannot be null!");

        LOGGER.debug("CustomerDetailsChanged event received: {}", evt);
    }
}
