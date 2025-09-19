package hu.hirannor.hexagonal.application.service.order.error;

public class OrderCannotBeCreatedWithoutAddress extends RuntimeException {
    public OrderCannotBeCreatedWithoutAddress(final String message) {
        super(message);
    }
}
