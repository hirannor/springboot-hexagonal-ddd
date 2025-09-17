package hu.hirannor.hexagonal.domain.order.events;

import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record OrderCreated(EventId id, OrderId orderId, CustomerId customerId) implements DomainEvent {

    public static OrderCreated create(final OrderId orderId, final CustomerId customerId) {
        return new OrderCreated(EventId.generate(), orderId, customerId);
    }
}
