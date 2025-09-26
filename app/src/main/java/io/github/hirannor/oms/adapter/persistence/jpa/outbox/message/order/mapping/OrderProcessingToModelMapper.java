package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order.OrderProcessingModel;
import io.github.hirannor.oms.domain.order.events.OrderProcessing;
import org.springframework.stereotype.Component;

@Component(value = "OrderProcessingToModelMapper")
public class OrderProcessingToModelMapper implements MessageMapper<OrderProcessing, OrderProcessingModel> {
    public OrderProcessingToModelMapper() {
    }

    @Override
    public OrderProcessingModel apply(final OrderProcessing evt) {
        if (evt == null) return null;

        return new OrderProcessingModel(evt.id(), evt.orderId().asText(), evt.customerId().asText());
    }

    @Override
    public Class<OrderProcessing> messageType() {
        return OrderProcessing.class;
    }
}
