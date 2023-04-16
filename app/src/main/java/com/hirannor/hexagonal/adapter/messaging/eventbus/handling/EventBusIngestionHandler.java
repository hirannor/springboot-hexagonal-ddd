package com.hirannor.hexagonal.adapter.messaging.eventbus.handling;

import com.hirannor.hexagonal.domain.customer.CustomerAdded;
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

    EventBusIngestionHandler() {
    }

    @Async
    @TransactionalEventListener
    public void handle(final CustomerAdded evt) {
        LOGGER.debug("CustomerAdded event received: {}", evt);
    }

}