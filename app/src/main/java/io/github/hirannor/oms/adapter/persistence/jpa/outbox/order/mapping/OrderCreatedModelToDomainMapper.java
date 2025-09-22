package io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderCreatedModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderCreated;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.function.Function;

public class OrderCreatedModelToDomainMapper implements Function<OrderCreatedModel, OrderCreated> {
    public OrderCreatedModelToDomainMapper() {}

    @Override
    public OrderCreated apply(final OrderCreatedModel model) {
        if (model == null) return null;

        return OrderCreated.recreate(
                MessageId.from(model.eventId()),
                OrderId.from(model.orderId()),
                CustomerId.from(model.customerId())
        );
    }
}
