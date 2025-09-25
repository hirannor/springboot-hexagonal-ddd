package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.ProductQuantityModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.OrderPaymentExpiredModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderPaymentExpired;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class OrderPaymentExpiredModelToDomainMapper implements DomainEventModelMapper<OrderPaymentExpiredModel, OrderPaymentExpired> {
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

    @Override
    public Class<OrderPaymentExpiredModel> eventType() {
        return OrderPaymentExpiredModel.class;
    }
}
