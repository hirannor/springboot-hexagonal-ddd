package io.github.hirannor.oms.application.usecase.payment;

public interface PaymentCallbackHandling {
    void handle(HandlePaymentCallback command);
}
