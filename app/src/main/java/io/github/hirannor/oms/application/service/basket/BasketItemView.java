package io.github.hirannor.oms.application.service.basket;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.product.ProductId;

public record BasketItemView(
        ProductId productId,
        String name,
        String description,
        Money price,
        int quantity,
        Money lineTotal
) {
}