package hu.hirannor.hexagonal.application.usecase.basket;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.basket.Basket;

import java.util.List;
import java.util.Optional;

public interface BasketDisplaying {
    Optional<Basket> displayBy(CustomerId customer);
    List<Basket> displayAll();
}
