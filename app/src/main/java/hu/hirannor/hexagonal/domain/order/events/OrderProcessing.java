package hu.hirannor.hexagonal.domain.order.events;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record OrderProcessing(EventId id, OrderId orderId, CustomerId customerId) implements DomainEvent {

    public static OrderProcessing record(final OrderId orderId, final CustomerId customerId) {
        return new OrderProcessing(EventId.generate(), orderId, customerId);
    }
}
