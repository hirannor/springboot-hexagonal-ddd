package hu.hirannor.hexagonal.domain.basket.events;

import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.domain.basket.BasketItem;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

import java.time.Instant;
import java.util.List;

public record BasketCheckedOut(
        EventId id,
        CustomerId customerId,
        List<BasketItem> items,
        Instant occurredAt
) implements DomainEvent {

    public static BasketCheckedOut record(final CustomerId customerId, final List<BasketItem> items) {
        return new BasketCheckedOut(EventId.generate(), customerId, items, Instant.now());
    }
}
