package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.OrderShippedModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderShipped;
import org.springframework.stereotype.Component;

@Component
public class OrderShippedModelToDomainMapper implements MessageModelMapper<OrderShippedModel, OrderShipped> {
    public OrderShippedModelToDomainMapper() {
    }

    @Override
    public OrderShipped apply(final OrderShippedModel model) {
        if (model == null) return null;

        return OrderShipped.recreate(
                model.id(),
                OrderId.from(model.orderId()),
                CustomerId.from(model.customerId())
        );
    }

    @Override
    public Class<OrderShippedModel> messageType() {
        return OrderShippedModel.class;
    }
}
