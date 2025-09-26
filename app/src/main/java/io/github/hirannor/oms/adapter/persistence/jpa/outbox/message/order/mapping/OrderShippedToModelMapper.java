package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order.OrderShippedModel;
import io.github.hirannor.oms.domain.order.events.OrderShipped;
import org.springframework.stereotype.Component;

@Component(value = "OrderShippedToModelMapper")
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
