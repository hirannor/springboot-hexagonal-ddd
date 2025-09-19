package hu.hirannor.hexagonal.application.service.order.error;

public class OrderNotFound extends RuntimeException {
    public OrderNotFound(final String message) {
        super(message);
    }
}
