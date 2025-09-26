package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.ProductQuantityModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order.OrderPaymentExpiredModel;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.events.OrderPaymentExpired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class OrderPaymentExpiredToModelMapper implements MessageMapper<OrderPaymentExpired, OrderPaymentExpiredModel> {

    public OrderPaymentExpiredToModelMapper() {
    }

    @Override
    public OrderPaymentExpiredModel apply(final OrderPaymentExpired evt) {
        if (evt == null) return null;


        final List<ProductQuantityModel> products = evt.products()
                .stream()
                .map(toModel())
                .toList();

        return new OrderPaymentExpiredModel(evt.id(),
                evt.orderId().asText(),
                evt.customer().asText(),
                products
        );
    }

    @Override
    public Class<OrderPaymentExpired> messageType() {
        return OrderPaymentExpired.class;
    }

    private Function<ProductQuantity, ProductQuantityModel> toModel() {
        return productQuantity -> ProductQuantityModel.of(productQuantity.productId().asText(), productQuantity.quantity());
    }

}
