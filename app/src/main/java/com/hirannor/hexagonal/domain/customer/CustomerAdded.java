package com.hirannor.hexagonal.domain.customer;

import com.hirannor.hexagonal.infrastructure.event.DomainEvent;
import com.hirannor.hexagonal.infrastructure.event.Event;
import com.hirannor.hexagonal.infrastructure.event.EventId;

import java.time.Instant;

public record CustomerAdded(EventId id, Instant registeredAt, CustomerId userId) implements DomainEvent {

    public static CustomerAdded issue(final CustomerId userId) {
        return new CustomerAdded(EventId.generate(), Event.now(), userId);
    }

    public static CustomerAdded recreate(final EventId id, final Instant registeredAt, final CustomerId userId) {
        return new CustomerAdded(id, registeredAt, userId);

    }

}
