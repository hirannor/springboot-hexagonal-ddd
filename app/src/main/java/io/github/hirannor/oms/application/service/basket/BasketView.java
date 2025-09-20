package io.github.hirannor.oms.application.service.basket;

import io.github.hirannor.oms.domain.basket.BasketId;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.Money;

import java.util.List;

public record BasketView(
    BasketId basketId,
    CustomerId customerId,
    List<BasketItemView> items,
    Money totalPrice
) {}