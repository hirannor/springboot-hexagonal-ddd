package io.github.hirannor.oms.domain.basket.events;


import io.github.hirannor.oms.domain.basket.BasketId;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.event.EventId;

public record BasketCreated(
        EventId id,
        BasketId basket,
        CustomerId customer) implements DomainEvent {

    public static BasketCreated record(final BasketId basket, final CustomerId customerId) {
        return new BasketCreated(EventId.generate(), basket, customerId);
    }
}