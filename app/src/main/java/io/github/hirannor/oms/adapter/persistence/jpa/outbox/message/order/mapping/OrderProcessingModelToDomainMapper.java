package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order.OrderProcessingModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderProcessing;
import org.springframework.stereotype.Component;

@Component(value = "OrderProcessingModelToDomainMapper")
public class OrderProcessingModelToDomainMapper implements MessageModelMapper<OrderProcessingModel, OrderProcessing> {
    public OrderProcessingModelToDomainMapper() {
    }

    @Override
    public OrderProcessing apply(final OrderProcessingModel model) {
        if (model == null) return null;

        return OrderProcessing.recreate(
                model.id(),
                OrderId.from(model.orderId()),
                CustomerId.from(model.customerId())
        );
    }

    @Override
    public Class<OrderProcessingModel> messageType() {
        return OrderProcessingModel.class;
    }
}
