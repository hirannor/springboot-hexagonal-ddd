package io.github.hirannor.oms.domain.basket.events;

import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.time.Instant;
import java.util.List;

public record BasketCheckedOut(
        MessageId id,
        CustomerId customerId,
        List<BasketItem> items,
        Instant occurredAt
) implements DomainEvent {

    public static BasketCheckedOut record(final CustomerId customerId, final List<BasketItem> items) {
        return new BasketCheckedOut(Message.generateId(), customerId, items, Instant.now());
    }

    public static BasketCheckedOut recreate(final MessageId id, final CustomerId customerId, final List<BasketItem> items) {
        return new BasketCheckedOut(id, customerId, items, Instant.now());
    }
}
