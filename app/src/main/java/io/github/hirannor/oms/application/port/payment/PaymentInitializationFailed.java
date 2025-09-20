package io.github.hirannor.oms.application.port.payment;

public class PaymentInitializationFailed extends RuntimeException {
    public PaymentInitializationFailed(final String message) {
        super(message);
    }
}
