package io.github.hirannor.oms.domain.order.events;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record OrderDelivered(MessageId id, OrderId orderId, CustomerId customerId) implements DomainEvent {

    public static OrderDelivered record(final OrderId orderId, final CustomerId customerId) {
        return new OrderDelivered(Message.generateId(), orderId, customerId);
    }
}