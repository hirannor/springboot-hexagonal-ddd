package io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderProcessingModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderProcessing;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.function.Function;

public class OrderProcessingModelToDomainMapper implements Function<OrderProcessingModel, OrderProcessing> {
    public OrderProcessingModelToDomainMapper() {
    }

    @Override
    public OrderProcessing apply(final OrderProcessingModel model) {
        if (model == null) return null;

        return OrderProcessing.recreate(
                MessageId.from(model.eventId()),
                OrderId.from(model.orderId()),
                CustomerId.from(model.customerId())
        );
    }
}
