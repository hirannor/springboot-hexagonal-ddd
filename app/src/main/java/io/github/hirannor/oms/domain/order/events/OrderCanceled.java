package io.github.hirannor.oms.domain.order.events;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record OrderCanceled(MessageId id, OrderId orderId, CustomerId customerId) implements DomainEvent {

    public static OrderCanceled record(final OrderId orderId, final CustomerId customerId) {
        return new OrderCanceled(Message.generateId(), orderId, customerId);
    }
}
