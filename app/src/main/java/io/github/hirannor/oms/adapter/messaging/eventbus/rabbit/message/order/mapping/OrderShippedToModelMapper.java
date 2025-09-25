package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.OrderShippedModel;
import io.github.hirannor.oms.domain.order.events.OrderShipped;
import org.springframework.stereotype.Component;

@Component
public class OrderShippedToModelMapper implements DomainEventMapper<OrderShipped, OrderShippedModel> {
    public OrderShippedToModelMapper() {
    }

    @Override
    public OrderShippedModel apply(final OrderShipped evt) {
        if (evt == null) return null;

        return new OrderShippedModel(evt.id().asText(), evt.orderId().asText(), evt.customerId().asText());
    }

    @Override
    public Class<OrderShipped> eventType() {
        return OrderShipped.class;
    }
}
