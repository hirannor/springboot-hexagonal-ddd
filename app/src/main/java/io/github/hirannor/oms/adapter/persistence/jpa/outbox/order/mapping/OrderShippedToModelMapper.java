package io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderShippedModel;
import io.github.hirannor.oms.domain.order.events.OrderShipped;

import java.util.function.Function;

public class OrderShippedToModelMapper implements Function<OrderShipped, OrderShippedModel> {
    public OrderShippedToModelMapper() {}

    @Override
    public OrderShippedModel apply(final OrderShipped evt) {
        if (evt == null) return null;

        return new OrderShippedModel(evt.id().asText(), evt.orderId().asText(), evt.customerId().asText());
    }
}
