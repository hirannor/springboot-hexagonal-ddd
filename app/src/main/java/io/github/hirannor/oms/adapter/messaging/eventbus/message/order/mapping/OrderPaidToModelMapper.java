package io.github.hirannor.oms.adapter.messaging.eventbus.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.OrderPaidModel;
import io.github.hirannor.oms.domain.order.events.OrderPaid;

import java.util.function.Function;

public class OrderPaidToModelMapper implements Function<OrderPaid, OrderPaidModel> {
    public OrderPaidToModelMapper() {}

    @Override
    public OrderPaidModel apply(final OrderPaid evt) {
        if (evt == null) return null;

        return new OrderPaidModel(evt.id().asText(),
                evt.orderId().asText(),
                evt.customerId().asText()
        );
    }
}
