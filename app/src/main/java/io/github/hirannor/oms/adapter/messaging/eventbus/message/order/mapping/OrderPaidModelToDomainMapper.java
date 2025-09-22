package io.github.hirannor.oms.adapter.messaging.eventbus.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.OrderPaidModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderPaid;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.function.Function;

public class OrderPaidModelToDomainMapper implements Function<OrderPaidModel, OrderPaid> {
    public OrderPaidModelToDomainMapper() {}

    @Override
    public OrderPaid apply(final OrderPaidModel model) {
        if (model == null) return null;

        return OrderPaid.recreate(
                MessageId.from(model.eventId()),
                OrderId.from(model.orderId()),
                CustomerId.from(model.customerId())
        );
    }
}
