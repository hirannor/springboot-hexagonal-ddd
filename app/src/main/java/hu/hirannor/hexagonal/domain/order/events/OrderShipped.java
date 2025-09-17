package hu.hirannor.hexagonal.domain.order.events;

import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record OrderShipped(EventId id, OrderId orderId, CustomerId customerId) implements DomainEvent {

    public static OrderShipped record(final OrderId orderId, final CustomerId customerId) {
        return new OrderShipped(EventId.generate(), orderId, customerId);
    }
}
