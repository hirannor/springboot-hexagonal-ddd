package io.github.hirannor.oms.application.service.order.error;

public class OrderNotFound extends RuntimeException {
    public OrderNotFound(final String message) {
        super(message);
    }
}
