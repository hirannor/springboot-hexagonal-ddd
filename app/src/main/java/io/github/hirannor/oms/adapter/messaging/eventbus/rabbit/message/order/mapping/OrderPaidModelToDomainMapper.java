package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.ProductQuantityModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.OrderPaidModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.events.OrderPaid;
import io.github.hirannor.oms.domain.product.ProductId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class OrderPaidModelToDomainMapper implements MessageModelMapper<OrderPaidModel, OrderPaid> {
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
                model.id(),
                OrderId.from(model.orderId()),
                CustomerId.from(model.customerId()),
                products
        );
    }

    private Function<ProductQuantityModel, ProductQuantity> toDomain() {
        return productQuantity -> ProductQuantity.of(ProductId.from(productQuantity.productId()), productQuantity.quantity());
    }

    @Override
    public Class<OrderPaidModel> messageType() {
        return OrderPaidModel.class;
    }
}
