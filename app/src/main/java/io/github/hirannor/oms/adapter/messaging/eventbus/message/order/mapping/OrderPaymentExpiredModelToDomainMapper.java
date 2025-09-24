package io.github.hirannor.oms.adapter.messaging.eventbus.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.ProductQuantityModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.OrderPaymentExpiredModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderPaymentExpired;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.List;
import java.util.function.Function;

public class OrderPaymentExpiredModelToDomainMapper implements Function<OrderPaymentExpiredModel, OrderPaymentExpired> {
    public OrderPaymentExpiredModelToDomainMapper() {
    }

    @Override
    public OrderPaymentExpired apply(final OrderPaymentExpiredModel model) {
        if (model == null) return null;

        final List<ProductQuantity> products = model.products()
                .stream()
                .map(toDomain())
                .toList();


        return OrderPaymentExpired.recreate(
                MessageId.from(model.eventId()),
                OrderId.from(model.orderId()),
                CustomerId.from(model.customerId()),
                products
        );
    }

    private Function<ProductQuantityModel, ProductQuantity> toDomain() {
        return productQuantity -> ProductQuantity.of(ProductId.from(productQuantity.productId()), productQuantity.quantity());
    }
}
