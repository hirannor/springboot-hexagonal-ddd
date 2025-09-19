package hu.hirannor.hexagonal.application.service.basket;

import hu.hirannor.hexagonal.domain.basket.BasketId;
import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.domain.core.valueobject.Money;
import java.util.List;

public record BasketView(
    BasketId basketId,
    CustomerId customerId,
    List<BasketItemView> items,
    Money totalPrice
) {}