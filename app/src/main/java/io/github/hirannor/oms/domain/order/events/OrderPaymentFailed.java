package io.github.hirannor.oms.domain.order.events;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record OrderPaymentFailed(MessageId id, CustomerId customer, OrderId orderId) implements DomainEvent {
    public static OrderPaymentFailed record(final OrderId orderId, final CustomerId customer) {
        return new OrderPaymentFailed(Message.generateId(), customer, orderId);
    }
}