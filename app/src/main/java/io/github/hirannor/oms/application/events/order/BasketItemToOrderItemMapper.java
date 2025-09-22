package io.github.hirannor.oms.application.events.order;

import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.order.OrderItem;

import java.util.function.Function;

public class BasketItemToOrderItemMapper implements Function<BasketItem, OrderItem> {

    public BasketItemToOrderItemMapper() {
    }

    @Override
    public OrderItem apply(final BasketItem basketItem) {
        if (basketItem == null) return null;

        return OrderItem.create(
                basketItem.productId(),
                basketItem.quantity(),
                basketItem.price()
        );
    }
}
