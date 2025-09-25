package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.OrderProcessingModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderProcessing;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessingModelToDomainMapper implements DomainEventModelMapper<OrderProcessingModel, OrderProcessing> {
    public OrderProcessingModelToDomainMapper() {
    }

    @Override
    public OrderProcessing apply(final OrderProcessingModel model) {
        if (model == null) return null;

        return OrderProcessing.recreate(
                MessageId.from(model.eventId()),
                OrderId.from(model.orderId()),
                CustomerId.from(model.customerId())
        );
    }

    @Override
    public Class<OrderProcessingModel> eventType() {
        return OrderProcessingModel.class;
    }
}
