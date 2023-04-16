package com.hirannor.hexagonal.application.service;

import com.hirannor.hexagonal.application.port.messaging.MessagePublisher;
import com.hirannor.hexagonal.application.usecase.eventing.EventPublishing;
import com.hirannor.hexagonal.infrastructure.event.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(
        propagation = Propagation.REQUIRES_NEW,
        isolation = Isolation.REPEATABLE_READ
)
class EventPublisherService implements EventPublishing {

    private static final Logger LOGGER = LogManager.getLogger(
            EventPublisherService.class
    );

    private final MessagePublisher messages;

    EventPublisherService(final MessagePublisher messages) {
        this.messages = messages;
    }

    @Override
    public void publish(final List<? extends Event> events) {
        if (events == null) throw new IllegalArgumentException(
                "Event list cannot be null!"
        );

        LOGGER.info("Publishing events: {}", events);

        messages.publish(events);
    }
}
