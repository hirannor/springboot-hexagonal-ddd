package io.github.hirannor.oms.application.usecase.order;

public interface OrderPaymentProcessing {
    void startProcessing(StartOrderPaymentProcessing cmd);
}