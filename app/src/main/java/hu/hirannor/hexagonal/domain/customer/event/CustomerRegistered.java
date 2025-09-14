package hu.hirannor.hexagonal.domain.customer.event;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.Event;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

import java.time.Instant;

/**
 * Domain event, which is fired when a customer has been registered.
 *
 * @param id           {@link EventId} unique identifier of event
 * @param registeredAt {@link Instant} registration time of event
 * @param userId       {@link CustomerId} unique identifier of customer
 * @author Mate Karolyi
 */
public record CustomerRegistered(EventId id, Instant registeredAt, CustomerId userId) implements DomainEvent {

    /**
     * Issues a {@link CustomerRegistered} domain event.
     *
     * @param userId {@link CustomerId}
     * @return an instance of {@link CustomerRegistered} domain event
     */
    public static CustomerRegistered issue(final CustomerId userId) {
        return new CustomerRegistered(EventId.generate(), Event.now(), userId);
    }

}
