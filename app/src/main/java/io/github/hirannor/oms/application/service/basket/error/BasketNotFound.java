package io.github.hirannor.oms.application.service.basket.error;


public class BasketNotFound extends RuntimeException {
    public BasketNotFound(final String message) {
        super(message);
    }
}
