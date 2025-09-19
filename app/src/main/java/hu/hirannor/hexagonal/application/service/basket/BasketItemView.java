package hu.hirannor.hexagonal.application.service.basket;

import hu.hirannor.hexagonal.domain.core.valueobject.Money;
import hu.hirannor.hexagonal.domain.product.ProductId;

public record BasketItemView(
    ProductId productId,
    String name,
    String description,
    Money price,
    int quantity,
    Money lineTotal
) {}