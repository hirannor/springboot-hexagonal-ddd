package hu.hirannor.hexagonal.application.usecase.basket;

import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.basket.command.CreateBasket;

public interface BasketCreation {
    Basket create(CreateBasket basket);
}
