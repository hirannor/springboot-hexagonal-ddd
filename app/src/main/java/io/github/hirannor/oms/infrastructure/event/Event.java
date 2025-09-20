package io.github.hirannor.oms.infrastructure.event;

import io.github.hirannor.oms.infrastructure.messaging.Message;

import java.time.Instant;

/**
 * Base interface for events.
 *
 * @author Mate Karolyi
 */
public interface Event extends Message {

    /**
     * Obtains the current instant from the system clock.
     *
     * @return {@link Instant}
     */
    static Instant now() {
        return Instant.now();
    }

    /**
     * Retrieves the value of event
     *
     * @return a {@link EventId}
     */
    EventId id();

}
