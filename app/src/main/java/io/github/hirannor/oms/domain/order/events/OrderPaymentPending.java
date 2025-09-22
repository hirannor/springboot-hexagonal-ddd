package io.github.hirannor.oms.domain.order.events;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record OrderPaymentPending(MessageId id, OrderId orderId, CustomerId customer) implements DomainEvent {
    public static OrderPaymentPending record(final OrderId orderId, final CustomerId customer) {
        return new OrderPaymentPending(Message.generateId(), orderId, customer);
    }
}