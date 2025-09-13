package hu.hirannor.hexagonal.domain.order.events;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record OrderPaid(EventId id, CustomerId customer, OrderId orderId) implements DomainEvent {

    public static OrderPaid record(final CustomerId customer, final OrderId orderId) {
        return new OrderPaid(EventId.generate(), customer, orderId);
    }
}
