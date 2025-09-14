package hu.hirannor.hexagonal.application.usecase.basket;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.basket.BasketId;

import java.util.List;
import java.util.Optional;

public interface BasketDisplaying {
    Optional<Basket> displayBy(CustomerId customer);
    Optional<Basket> displayBy(BasketId basket);
    List<Basket> displayAll();
}
