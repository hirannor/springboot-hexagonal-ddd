package io.github.hirannor.oms.domain.order.events;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.event.EventId;

public record OrderRefunded(EventId id, OrderId orderId, CustomerId customerId) implements DomainEvent {

    public static OrderRefunded record(final OrderId orderId, final CustomerId customerId) {
        return new OrderRefunded(EventId.generate(), orderId, customerId);
    }
}