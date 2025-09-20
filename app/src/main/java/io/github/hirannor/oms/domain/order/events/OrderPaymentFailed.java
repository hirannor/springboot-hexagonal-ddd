package io.github.hirannor.oms.domain.order.events;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.event.EventId;

public record OrderPaymentFailed(EventId id, CustomerId customer, OrderId orderId) implements DomainEvent {
    public static OrderPaymentFailed record(final OrderId orderId, final CustomerId customer) {
        return new OrderPaymentFailed(EventId.generate(), customer, orderId);
    }
}