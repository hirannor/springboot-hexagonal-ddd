package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order.OrderCreatedModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderCreated;
import org.springframework.stereotype.Component;

@Component(value = "OrderCreatedModelToDomainMapper")
public class OrderCreatedModelToDomainMapper implements MessageModelMapper<OrderCreatedModel, OrderCreated> {
    public OrderCreatedModelToDomainMapper() {
    }

    @Override
    public OrderCreated apply(final OrderCreatedModel model) {
        if (model == null) return null;

        return OrderCreated.recreate(
                model.id(),
                OrderId.from(model.orderId()),
                CustomerId.from(model.customerId())
        );
    }

    @Override
    public Class<OrderCreatedModel> messageType() {
        return OrderCreatedModel.class;
    }
}
