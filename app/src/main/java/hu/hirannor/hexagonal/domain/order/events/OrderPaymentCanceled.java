package hu.hirannor.hexagonal.domain.order.events;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record OrderPaymentCanceled(EventId id, CustomerId customer, OrderId orderId) implements DomainEvent {
    public static OrderPaymentCanceled record(final CustomerId customer, final OrderId orderId) {
        return new OrderPaymentCanceled(EventId.generate(), customer, orderId);
    }
}