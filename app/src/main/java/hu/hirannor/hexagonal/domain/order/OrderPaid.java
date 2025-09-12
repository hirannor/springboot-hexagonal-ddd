package hu.hirannor.hexagonal.domain.order;

import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record OrderPaid(EventId id, OrderId orderId) implements DomainEvent {

    public static OrderPaid create(final OrderId orderId) {
        return new OrderPaid(EventId.generate(), orderId);
    }
}
