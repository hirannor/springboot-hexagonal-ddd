package io.github.hirannor.oms.domain.order.events;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record OrderPaymentCanceled(MessageId id, OrderId orderId, CustomerId customer) implements DomainEvent {
    public static OrderPaymentCanceled record(final OrderId orderId, final CustomerId customer) {
        return new OrderPaymentCanceled(Message.generateId(), orderId, customer);
    }
}