package io.github.hirannor.oms.domain.order.events;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.event.EventId;

public record OrderCanceled(EventId id, OrderId orderId, CustomerId customerId) implements DomainEvent {

    public static OrderCanceled record(final OrderId orderId, final CustomerId customerId) {
        return new OrderCanceled(EventId.generate(), orderId, customerId);
    }
}
