package io.github.hirannor.oms.domain.basket;

public class BasketIsEmpty extends RuntimeException {
    public BasketIsEmpty(final String message) {
        super(message);
    }
}
