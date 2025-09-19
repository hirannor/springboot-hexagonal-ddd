package hu.hirannor.hexagonal.application.usecase.basket;

import hu.hirannor.hexagonal.application.service.basket.BasketView;
import hu.hirannor.hexagonal.domain.basket.BasketId;
import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import java.util.List;
import java.util.Optional;

public interface BasketDisplaying {
    Optional<BasketView> displayBy(CustomerId customer);
    Optional<BasketView> displayBy(BasketId basket);
    List<BasketView> displayAll();
}
