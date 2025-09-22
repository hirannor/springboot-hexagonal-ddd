package io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderCreatedModel;
import io.github.hirannor.oms.domain.order.events.OrderCreated;

import java.util.function.Function;

public class OrderCreatedToModelMapper implements Function<OrderCreated, OrderCreatedModel> {
    public OrderCreatedToModelMapper() {}

    @Override
    public OrderCreatedModel apply(final OrderCreated evt) {
        if (evt == null) return null;

        return new OrderCreatedModel(evt.id().asText(),
                evt.orderId().asText(),
                evt.customerId().asText()
        );
    }
}
