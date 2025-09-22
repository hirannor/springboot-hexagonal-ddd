package io.github.hirannor.oms.application.service.order.error;

public class OrderCannotBeCreatedWithoutBasketCheckout extends RuntimeException {
    public OrderCannotBeCreatedWithoutBasketCheckout(final String message) {
        super(message);
    }
}
