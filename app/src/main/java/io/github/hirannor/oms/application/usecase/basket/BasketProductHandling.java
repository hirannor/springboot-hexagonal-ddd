package io.github.hirannor.oms.application.usecase.basket;

import io.github.hirannor.oms.domain.basket.command.AddBasketItem;
import io.github.hirannor.oms.domain.basket.command.RemoveBasketItem;

public interface BasketProductHandling {
    void add(final AddBasketItem command);

    void remove(final RemoveBasketItem command);
}
