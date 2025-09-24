package io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.ProductQuantityModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderPaymentExpiredModel;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.events.OrderPaymentExpired;

import java.util.List;
import java.util.function.Function;

public class OrderPaymentExpiredToModelMapper implements Function<OrderPaymentExpired, OrderPaymentExpiredModel> {

    public OrderPaymentExpiredToModelMapper() {
    }

    @Override
    public OrderPaymentExpiredModel apply(final OrderPaymentExpired evt) {
        if (evt == null) return null;


        final List<ProductQuantityModel> products = evt.products()
                .stream()
                .map(toModel())
                .toList();

        return new OrderPaymentExpiredModel(evt.id().asText(),
                evt.orderId().asText(),
                evt.customer().asText(),
                products
        );
    }

    private Function<ProductQuantity, ProductQuantityModel> toModel() {
        return productQuantity -> ProductQuantityModel.of(productQuantity.productId().asText(), productQuantity.quantity());
    }
}
