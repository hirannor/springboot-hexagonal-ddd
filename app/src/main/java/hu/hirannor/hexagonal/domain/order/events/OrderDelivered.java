package hu.hirannor.hexagonal.domain.order.events;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record OrderDelivered(EventId id, OrderId orderId, CustomerId customerId) implements DomainEvent {

    public static OrderDelivered record(final OrderId orderId, final CustomerId customerId) {
        return new OrderDelivered(EventId.generate(), orderId, customerId);
    }
}