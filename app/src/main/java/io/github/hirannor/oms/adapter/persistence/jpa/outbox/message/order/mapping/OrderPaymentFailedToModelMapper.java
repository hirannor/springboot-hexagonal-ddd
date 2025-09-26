package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.ProductQuantityModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order.OrderPaymentFailedModel;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.events.OrderPaymentFailed;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component(value = "OrderPaymentFailedToModelMapper")
public class OrderPaymentFailedToModelMapper implements MessageMapper<OrderPaymentFailed, OrderPaymentFailedModel> {

    public OrderPaymentFailedToModelMapper() {
    }

    @Override
    public OrderPaymentFailedModel apply(final OrderPaymentFailed model) {
        if (model == null) return null;


        final List<ProductQuantityModel> products = model.products()
                .stream()
                .map(toModel())
                .toList();

        return new OrderPaymentFailedModel(model.id(),
                model.orderId().asText(),
                model.customer().asText(),
                products
        );
    }

    private Function<ProductQuantity, ProductQuantityModel> toModel() {
        return productQuantity -> ProductQuantityModel.of(productQuantity.productId().asText(), productQuantity.quantity());
    }

    @Override
    public Class<OrderPaymentFailed> messageType() {
        return OrderPaymentFailed.class;
    }
}
