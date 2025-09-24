package io.github.hirannor.oms.adapter.messaging.eventbus.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.ProductQuantityModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.OrderPaymentFailedModel;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.events.OrderPaymentFailed;

import java.util.List;
import java.util.function.Function;

public class OrderPaymentFailedToModelMapper implements Function<OrderPaymentFailed, OrderPaymentFailedModel> {

    public OrderPaymentFailedToModelMapper() {
    }

    @Override
    public OrderPaymentFailedModel apply(final OrderPaymentFailed model) {
        if (model == null) return null;


        final List<ProductQuantityModel> products = model.products()
                .stream()
                .map(toModel())
                .toList();

        return new OrderPaymentFailedModel(model.id().asText(),
                model.orderId().asText(),
                model.customer().asText(),
                products
        );
    }

    private Function<ProductQuantity, ProductQuantityModel> toModel() {
        return productQuantity -> ProductQuantityModel.of(productQuantity.productId().asText(), productQuantity.quantity());
    }
}
