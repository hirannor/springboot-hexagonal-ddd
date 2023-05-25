package com.hirannor.hexagonal.domain.customer;

import com.hirannor.hexagonal.infrastructure.event.*;
import java.time.Instant;

public record CustomerDetailsChanged(EventId id, Instant registeredAt, CustomerId userId) implements DomainEvent {

    public static CustomerDetailsChanged issue(final CustomerId userId) {
        return new CustomerDetailsChanged(EventId.generate(), Event.now(), userId);
    }

}
