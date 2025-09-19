package hu.hirannor.hexagonal.application.service.basket;

import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.product.Product;
import hu.hirannor.hexagonal.domain.product.ProductId;
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
