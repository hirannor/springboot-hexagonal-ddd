package io.github.hirannor.oms.application.usecase.basket;

import io.github.hirannor.oms.application.service.basket.BasketView;
import io.github.hirannor.oms.domain.basket.BasketId;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;

import java.util.List;
import java.util.Optional;

public interface BasketDisplaying {
    Optional<BasketView> displayBy(CustomerId customer);

    Optional<BasketView> displayBy(BasketId basket);

    List<BasketView> displayAll();
}
