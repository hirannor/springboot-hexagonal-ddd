package hu.hirannor.hexagonal.application.events;

import hu.hirannor.hexagonal.domain.customer.event.PersonalDetailsChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class PersonalDetailsChanger {

    private static final Logger LOGGER = LogManager.getLogger(
        PersonalDetailsChanger.class
    );

    PersonalDetailsChanger() {}

    /**
     * Handles incoming {@link PersonalDetailsChanged} event.
     *
     * @param evt {@link PersonalDetailsChanged} to be handled
     */
    @TransactionalEventListener
    public void handle(final PersonalDetailsChanged evt) {
        if (evt == null) throw new IllegalArgumentException("Customer details changed event cannot be null!");

        LOGGER.debug("CustomerDetailsChanged event received: {}", evt);
    }
}
