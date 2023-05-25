package com.hirannor.hexagonal.adapter.messaging.eventbus.publisher;

import com.hirannor.hexagonal.application.port.messaging.MessagePublisher;
import com.hirannor.hexagonal.infrastructure.messaging.Message;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.*;

/**
 * Event bus message publisher implementation of {@link MessagePublisher}.
 * It publishes the messages through the {@link ApplicationEventPublisher} publisher.
 *
 * @author Mate Karolyi
 */
@Component
@Transactional(
    propagation = Propagation.MANDATORY,
    isolation = Isolation.REPEATABLE_READ
)
class EventBusMessagePublisher implements MessagePublisher {

    private static final Logger LOGGER = LogManager.getLogger(
        EventBusMessagePublisher.class
    );

    private final ApplicationEventPublisher springEvents;

    @Autowired
    EventBusMessagePublisher(
        final ApplicationEventPublisher applicationEventPublisher
    ) {
        this.springEvents = applicationEventPublisher;
    }

    /**
     * Publish messages through {@link ApplicationEventPublisher} publisher.
     *
     * @param messages to be published
     */
    @Override
    public void publish(final List<? extends Message> messages) {
        if (messages == null) throw new IllegalArgumentException("Messages cannot be null!");

        LOGGER.debug("Publishing messages: {}", messages);

        messages.forEach(springEvents::publishEvent);
    }

}