package io.github.hirannor.oms.domain.customer.event;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.event.EventId;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.time.Instant;

/**
 * Domain event, which is fired whenever a detail of a customer has been changed.
 *
 * @param id           {@link EventId} unique identifier of event
 * @param registeredAt {@link Instant} registration time of event
 * @param userId       {@link CustomerId} unique identifier of customer
 * @author Mate Karolyi
 */
public record PersonalDetailsChanged(MessageId id, Instant registeredAt, CustomerId userId) implements DomainEvent {

    /**
     * Issues a {@link PersonalDetailsChanged} domain event.
     *
     * @param userId {@link CustomerId}
     * @return an instance of {@link PersonalDetailsChanged} domain event
     */
    public static PersonalDetailsChanged issue(final CustomerId userId) {
        return new PersonalDetailsChanged(Message.generateId(), Instant.now(), userId);
    }

}
