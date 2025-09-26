package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.OrderShippedModel;
import io.github.hirannor.oms.domain.order.events.OrderShipped;
import org.springframework.stereotype.Component;

@Component
public class OrderShippedToModelMapper implements MessageMapper<OrderShipped, OrderShippedModel> {
    public OrderShippedToModelMapper() {
    }

    @Override
    public OrderShippedModel apply(final OrderShipped evt) {
        if (evt == null) return null;

        return new OrderShippedModel(evt.id(), evt.orderId().asText(), evt.customerId().asText());
    }

    @Override
    public Class<OrderShipped> messageType() {
        return OrderShipped.class;
    }
}
