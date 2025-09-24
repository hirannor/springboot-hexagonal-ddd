package io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.ProductQuantityModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderPaidModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderPaid;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.List;
import java.util.function.Function;

public class OrderPaidModelToDomainMapper implements Function<OrderPaidModel, OrderPaid> {
    public OrderPaidModelToDomainMapper() {
    }

    @Override
    public OrderPaid apply(final OrderPaidModel model) {
        if (model == null) return null;

        final List<ProductQuantity> products = model.products()
                .stream()
                .map(toDomain())
                .toList();

        return OrderPaid.recreate(
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
