package hu.hirannor.hexagonal.domain.basket.events;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.basket.BasketItem;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

import java.time.Instant;
import java.util.Set;

public record BasketCheckedOut(
        EventId id,
        CustomerId customerId,
        Set<BasketItem> items,
        Instant occurredAt
) implements DomainEvent {

    public static BasketCheckedOut record(final CustomerId customerId, final Set<BasketItem> items) {
        return new BasketCheckedOut(EventId.generate(), customerId, Set.copyOf(items), Instant.now());
    }
}
