package com.hirannor.hexagonal.infrastructure.event;

import com.hirannor.hexagonal.infrastructure.messaging.Message;

import java.time.Instant;

public interface Event extends Message {

    EventId id();

    Instant registeredAt();

    static EventId generateId() {
        return EventId.generate();
    }

    static Instant now() {
        return Instant.now();
    }

}
