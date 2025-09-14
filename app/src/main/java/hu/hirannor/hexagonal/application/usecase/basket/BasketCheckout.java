package hu.hirannor.hexagonal.application.usecase.basket;

import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.basket.command.CheckoutBasket;

public interface BasketCheckout {
    Basket checkout(CheckoutBasket command);
}
