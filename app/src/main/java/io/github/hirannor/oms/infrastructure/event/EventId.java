package io.github.hirannor.oms.infrastructure.event;

import java.util.UUID;

/**
 * Immutable record to hold event value
 *
 * @param id {@link UUID} unique identifier of event
 * @author Mate Karolyi
 */
public record EventId(UUID id) {
    /**
     * Default constructor
     *
     * @param id {@link UUID} unique identifier of event
     */
    public EventId {
        if (id == null) throw new IllegalArgumentException("EventId cannot be null!");
    }

    /**
     * Create an instance of {@link EventId}
     *
     * @param value {@link String} raw string value of a UUID notificationType
     * @return an instance of {@link EventId} notificationType
     */
    public static EventId from(final String value) {
        return new EventId(UUID.fromString(value));
    }

    /**
     * Generates an unique {@link EventId} event value.
     *
     * @return an instance of {@link EventId} notificationType
     */
    public static EventId generate() {
        return new EventId(UUID.randomUUID());
    }

}
