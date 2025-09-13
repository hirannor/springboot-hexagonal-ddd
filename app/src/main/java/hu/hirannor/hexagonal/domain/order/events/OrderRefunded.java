package hu.hirannor.hexagonal.domain.order.events;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record OrderRefunded(EventId id, OrderId orderId, CustomerId customerId) implements DomainEvent {

    public static OrderRefunded record(final OrderId orderId, final CustomerId customerId) {
        return new OrderRefunded(EventId.generate(), orderId, customerId);
    }
}