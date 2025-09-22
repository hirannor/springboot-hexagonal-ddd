package io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderProcessingModel;
import io.github.hirannor.oms.domain.order.events.OrderProcessing;

import java.util.function.Function;

public class OrderProcessingToModelMapper implements Function<OrderProcessing, OrderProcessingModel> {
    public OrderProcessingToModelMapper() {
    }

    @Override
    public OrderProcessingModel apply(final OrderProcessing evt) {
        if (evt == null) return null;

        return new OrderProcessingModel(evt.id().asText(), evt.orderId().asText(), evt.customerId().asText());
    }
}
