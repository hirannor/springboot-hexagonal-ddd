package io.github.hirannor.oms.application.service.basket;

import io.github.hirannor.oms.domain.basket.Basket;
import io.github.hirannor.oms.domain.product.Product;
import io.github.hirannor.oms.domain.product.ProductId;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class BasketToViewMapper implements BiFunction<Basket, Map<ProductId, Product>, BasketView> {

    public BasketToViewMapper() {}

    @Override
    public BasketView apply(final Basket basket, final Map<ProductId, Product> products) {
        final List<BasketItemView> itemViews = basket.items().stream()
            .map(item -> {
                final Product product = products.get(item.productId());
                return new BasketItemView(
                    item.productId(),
                    product.name(),
                    product.description(),
                    item.price(),
                    item.quantity(),
                    item.lineTotal()
                );
            })
            .toList();

        return new BasketView(
            basket.id(),
            basket.customer(),
            itemViews,
            basket.totalPrice()
        );
    }
}
