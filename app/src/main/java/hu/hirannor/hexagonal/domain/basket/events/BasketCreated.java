package hu.hirannor.hexagonal.domain.basket.events;


import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.domain.basket.BasketId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record BasketCreated(
        EventId id,
        BasketId basket,
        CustomerId customer) implements DomainEvent {

    public static BasketCreated record(final BasketId basket, final CustomerId customerId) {
        return new BasketCreated(EventId.generate(), basket, customerId);
    }
}