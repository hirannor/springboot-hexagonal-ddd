package io.github.hirannor.oms.application.usecase.basket;

import io.github.hirannor.oms.domain.basket.Basket;
import io.github.hirannor.oms.domain.basket.command.CreateBasket;

public interface BasketCreation {
    Basket create(CreateBasket basket);
}
