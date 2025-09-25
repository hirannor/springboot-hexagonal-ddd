package io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.ProductQuantityModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderPaymentFailedModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderPaymentFailed;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component(value = "OrderPaymentFailedModelToDomainMapper")
public class OrderPaymentFailedModelToDomainMapper implements DomainEventModelMapper<OrderPaymentFailedModel, OrderPaymentFailed> {
    public OrderPaymentFailedModelToDomainMapper() {
    }

    @Override
    public OrderPaymentFailed apply(final OrderPaymentFailedModel model) {
        if (model == null) return null;

        final List<ProductQuantity> products = model.products()
                .stream()
                .map(toDomain())
                .toList();


        return OrderPaymentFailed.recreate(
                MessageId.from(model.eventId()),
                OrderId.from(model.orderId()),
                CustomerId.from(model.customerId()),
                products
        );
    }

    private Function<ProductQuantityModel, ProductQuantity> toDomain() {
        return productQuantity -> ProductQuantity.of(ProductId.from(productQuantity.productId()), productQuantity.quantity());
    }

    @Override
    public Class<OrderPaymentFailedModel> eventType() {
        return OrderPaymentFailedModel.class;
    }
}
