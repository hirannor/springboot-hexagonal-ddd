package hu.hirannor.hexagonal.application.events;

import hu.hirannor.hexagonal.domain.basket.BasketItem;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;

import java.util.function.Function;

public class BasketItemToOrderedItemMapper implements Function<BasketItem, OrderedProduct> {

    @Override
    public OrderedProduct apply(final BasketItem item) {
        return OrderedProduct.create(
                item.productId(),
                item.quantity(),
                item.price()
        );
    }
}
