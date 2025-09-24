package io.github.hirannor.oms.domain.order.events;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.List;

public record OrderPaymentFailed(MessageId id, CustomerId customer, OrderId orderId,
                                 List<ProductQuantity> products) implements DomainEvent {
    public static OrderPaymentFailed record(final OrderId orderId, final CustomerId customerId, final List<ProductQuantity> products) {
        return new OrderPaymentFailed(Message.generateId(), customerId, orderId, products);
    }

    public static OrderPaymentFailed recreate(MessageId id, final OrderId orderId, final CustomerId customerId, final List<ProductQuantity> products) {
        return new OrderPaymentFailed(id, customerId, orderId, products);
    }
}