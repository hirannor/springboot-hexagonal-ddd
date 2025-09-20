package io.github.hirannor.oms.domain.order.events;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.event.EventId;

public record OrderPaymentCanceled(EventId id, OrderId orderId, CustomerId customer) implements DomainEvent {
    public static OrderPaymentCanceled record(final OrderId orderId, final CustomerId customer) {
        return new OrderPaymentCanceled(EventId.generate(), orderId, customer);
    }
}