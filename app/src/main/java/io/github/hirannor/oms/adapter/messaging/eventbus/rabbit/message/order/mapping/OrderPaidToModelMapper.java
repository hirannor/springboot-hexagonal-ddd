package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.ProductQuantityModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.OrderPaidModel;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.events.OrderPaid;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class OrderPaidToModelMapper implements MessageMapper<OrderPaid, OrderPaidModel> {
    public OrderPaidToModelMapper() {
    }

    @Override
    public OrderPaidModel apply(final OrderPaid evt) {
        if (evt == null) return null;


        final List<ProductQuantityModel> products = evt.products()
                .stream()
                .map(toModel())
                .toList();

        return new OrderPaidModel(evt.id(),
                evt.orderId().asText(),
                evt.customerId().asText(),
                products
        );
    }

    @Override
    public Class<OrderPaid> messageType() {
        return OrderPaid.class;
    }

    private Function<ProductQuantity, ProductQuantityModel> toModel() {
        return productQuantity -> ProductQuantityModel.of(productQuantity.productId().asText(), productQuantity.quantity());
    }
}
