package com.hirannor.hexagonal.adapter.messaging.eventbus.handling;

import com.hirannor.hexagonal.domain.customer.CustomerDetailsChanged;
import com.hirannor.hexagonal.domain.customer.CustomerRegistered;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class EventBusIngestionHandler {

    private static final Logger LOGGER = LogManager.getLogger(
            EventBusIngestionHandler.class
    );

    EventBusIngestionHandler() {}

    @Async
    @TransactionalEventListener
    public void handle(final CustomerRegistered evt) {
        LOGGER.debug("CustomerRegistered event received: {}", evt);
    }

    @Async
    @TransactionalEventListener
    public void handle(final CustomerDetailsChanged evt) {
        LOGGER.debug("CustomerDetailsChanged event received: {}", evt);
    }

}