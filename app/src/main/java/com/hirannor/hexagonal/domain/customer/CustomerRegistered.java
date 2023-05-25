package com.hirannor.hexagonal.domain.customer;

import com.hirannor.hexagonal.infrastructure.event.DomainEvent;
import com.hirannor.hexagonal.infrastructure.event.Event;
import com.hirannor.hexagonal.infrastructure.event.EventId;

import java.time.Instant;

public record CustomerRegistered(EventId id, Instant registeredAt, CustomerId userId) implements DomainEvent {

    public static CustomerRegistered issue(final CustomerId userId) {
        return new CustomerRegistered(EventId.generate(), Event.now(), userId);
    }

}
