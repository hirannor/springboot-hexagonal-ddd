package hu.hirannor.hexagonal.application.usecase.basket;

import hu.hirannor.hexagonal.application.service.basket.BasketView;
import hu.hirannor.hexagonal.domain.basket.command.CheckoutBasket;

public interface BasketCheckout {
    BasketView checkout(CheckoutBasket command);
}
