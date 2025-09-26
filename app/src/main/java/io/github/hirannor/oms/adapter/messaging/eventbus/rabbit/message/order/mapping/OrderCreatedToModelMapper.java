package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.OrderCreatedModel;
import io.github.hirannor.oms.domain.order.events.OrderCreated;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedToModelMapper implements MessageMapper<OrderCreated, OrderCreatedModel> {
    public OrderCreatedToModelMapper() {
    }

    @Override
    public OrderCreatedModel apply(final OrderCreated evt) {
        if (evt == null) return null;

        return new OrderCreatedModel(evt.id(),
                evt.orderId().asText(),
                evt.customerId().asText()
        );
    }

    @Override
    public Class<OrderCreated> messageType() {
        return OrderCreated.class;
    }
}
