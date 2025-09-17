package hu.hirannor.hexagonal.domain.order.events;

import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record OrderPaymentFailed(EventId id, CustomerId customer, OrderId orderId) implements DomainEvent {
    public static OrderPaymentFailed record(final OrderId orderId, final CustomerId customer) {
        return new OrderPaymentFailed(EventId.generate(), customer, orderId);
    }
}