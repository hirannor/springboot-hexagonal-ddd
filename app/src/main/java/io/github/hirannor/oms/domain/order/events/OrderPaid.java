package io.github.hirannor.oms.domain.order.events;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.List;

public record OrderPaid(MessageId id, OrderId orderId, CustomerId customerId,
                        List<ProductQuantity> products) implements DomainEvent {

    public static OrderPaid record(final OrderId orderId, final CustomerId customerId, final List<ProductQuantity> products) {
        return new OrderPaid(Message.generateId(), orderId, customerId, products);
    }

    public static OrderPaid recreate(MessageId id, final OrderId orderId, final CustomerId customerId, final List<ProductQuantity> products) {
        return new OrderPaid(id, orderId, customerId, products);
    }
}
