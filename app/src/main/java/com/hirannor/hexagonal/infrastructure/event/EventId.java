package com.hirannor.hexagonal.infrastructure.event;

import java.util.UUID;

public record EventId(UUID evtId) {
    public EventId {
        if (evtId == null) throw new IllegalArgumentException("EventId cannot be null!");
    }

    public static EventId from(final String value) {
        return new EventId(UUID.fromString(value));
    }

    public static EventId generate() {
        return new EventId(UUID.randomUUID());
    }

    public String asText() {
        return this.evtId.toString();
    }
}
