package hu.hirannor.hexagonal.domain.order.events;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record OrderPaymentPending(EventId id, OrderId orderId, CustomerId customer) implements DomainEvent {
    public static OrderPaymentPending record(final OrderId orderId, final CustomerId customer) {
        return new OrderPaymentPending(EventId.generate(), orderId, customer);
    }
}