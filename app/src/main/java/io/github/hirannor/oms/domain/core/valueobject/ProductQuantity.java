package io.github.hirannor.oms.domain.core.valueobject;

import io.github.hirannor.oms.domain.product.ProductId;

import java.util.Objects;

public record ProductQuantity(ProductId productId, int quantity) {
    public ProductQuantity {
        Objects.requireNonNull(productId, "ProductId cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
    }

    public static ProductQuantity of(ProductId productId, int quantity) {
        return new ProductQuantity(productId, quantity);
    }
}
