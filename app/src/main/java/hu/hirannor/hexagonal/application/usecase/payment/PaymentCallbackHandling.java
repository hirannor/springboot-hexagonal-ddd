package hu.hirannor.hexagonal.application.usecase.payment;

public interface PaymentCallbackHandling {
    void handle(HandlePaymentCallback command);
}
