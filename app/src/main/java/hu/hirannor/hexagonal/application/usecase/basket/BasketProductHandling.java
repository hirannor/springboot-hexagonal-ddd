package hu.hirannor.hexagonal.application.usecase.basket;

import hu.hirannor.hexagonal.domain.basket.command.AddBasketItem;
import hu.hirannor.hexagonal.domain.basket.command.RemoveBasketItem;

public interface BasketProductHandling {
    void add(final AddBasketItem command);
    void remove(final RemoveBasketItem command);
}
