package io.github.hirannor.oms.domain.order.events;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.event.EventId;

public record OrderProcessing(EventId id, OrderId orderId, CustomerId customerId) implements DomainEvent {

    public static OrderProcessing record(final OrderId orderId, final CustomerId customerId) {
        return new OrderProcessing(EventId.generate(), orderId, customerId);
    }
}
