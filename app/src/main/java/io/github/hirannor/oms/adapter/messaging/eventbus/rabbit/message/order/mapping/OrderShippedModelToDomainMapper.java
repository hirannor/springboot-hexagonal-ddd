package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.OrderShippedModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderShipped;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;
import org.springframework.stereotype.Component;

@Component
public class OrderShippedModelToDomainMapper implements DomainEventModelMapper<OrderShippedModel, OrderShipped> {
    public OrderShippedModelToDomainMapper() {
    }

    @Override
    public OrderShipped apply(final OrderShippedModel model) {
        if (model == null) return null;

        return OrderShipped.recreate(
                MessageId.from(model.eventId()),
                OrderId.from(model.orderId()),
                CustomerId.from(model.customerId())
        );
    }

    @Override
    public Class<OrderShippedModel> eventType() {
        return OrderShippedModel.class;
    }
}
