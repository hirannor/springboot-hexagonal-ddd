package io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderShippedModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderShipped;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.function.Function;

public class OrderShippedModelToDomainMapper implements Function<OrderShippedModel, OrderShipped> {
    public OrderShippedModelToDomainMapper() {}

    @Override
    public OrderShipped apply(final OrderShippedModel model) {
        if (model == null) return null;

        return OrderShipped.recreate(
                MessageId.from(model.eventId()),
                OrderId.from(model.orderId()),
                CustomerId.from(model.customerId())
        );
    }
}
