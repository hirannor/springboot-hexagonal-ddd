package io.github.hirannor.oms.domain.basket.events;


import io.github.hirannor.oms.domain.basket.BasketId;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record BasketCreated(
        MessageId id,
        BasketId basket,
        CustomerId customer) implements DomainEvent {

    public static BasketCreated record(final BasketId basket, final CustomerId customerId) {
        return new BasketCreated(Message.generateId(), basket, customerId);
    }
}