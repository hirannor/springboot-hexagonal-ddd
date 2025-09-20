package io.github.hirannor.oms.application.usecase.basket;

import io.github.hirannor.oms.application.service.basket.BasketView;
import io.github.hirannor.oms.domain.basket.command.CheckoutBasket;

public interface BasketCheckout {
    BasketView checkout(CheckoutBasket command);
}
