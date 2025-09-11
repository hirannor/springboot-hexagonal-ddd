package hu.hirannor.hexagonal.application.events;

import hu.hirannor.hexagonal.domain.customer.event.CustomerRegistered;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class CustomerRegisterer {
    private static final Logger LOGGER = LogManager.getLogger(
        CustomerRegisterer.class
    );

    CustomerRegisterer() {}

    /**
     * Handles incoming {@link CustomerRegistered} event.
     *
     * @param evt {@link CustomerRegistered} to be handled
     */
    @TransactionalEventListener
    public void handle(final CustomerRegistered evt) {
        if (evt == null) throw new IllegalArgumentException("Customer registered event cannot be null!");

        LOGGER.debug("CustomerRegistered event received: {}", evt);
    }
}
