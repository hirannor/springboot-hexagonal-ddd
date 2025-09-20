package io.github.hirannor.oms.domain.order;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.product.ProductId;

import java.util.Objects;

public record OrderItem(ProductId productId, int quantity, Money price) {

    public OrderItem {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(price);

        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");
        if (price.amount().signum() <= 0) throw new IllegalArgumentException("Price must be positive");
    }

    public static OrderItem create(final ProductId productId, final int quantity, final Money price) {
        return new OrderItem(productId, quantity, price);
    }

    public Money lineTotal() {
        return price.multiply(quantity);
    }
}
