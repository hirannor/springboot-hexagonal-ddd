package hu.hirannor.hexagonal.adapter.messaging.eventbus.publisher;

import hu.hirannor.hexagonal.application.port.messaging.MessagePublisher;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
import hu.hirannor.hexagonal.infrastructure.messaging.Message;
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
@DrivenAdapter
class EventBusMessagePublisher implements MessagePublisher {

    private static final Logger LOGGER = LogManager.getLogger(
        EventBusMessagePublisher.class
    );

    private final ApplicationEventPublisher springEvents;

    @Autowired
    EventBusMessagePublisher(
        final ApplicationEventPublisher springEvents
    ) {
        this.springEvents = springEvents;
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