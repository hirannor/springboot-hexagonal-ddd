package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.ProductQuantityModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order.OrderPaidModel;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.events.OrderPaid;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component(value = "OrderPaidToModelMapper")
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

    private Function<ProductQuantity, ProductQuantityModel> toModel() {
        return productQuantity -> ProductQuantityModel.of(productQuantity.productId().asText(), productQuantity.quantity());
    }

    @Override
    public Class<OrderPaid> messageType() {
        return OrderPaid.class;
    }
}
