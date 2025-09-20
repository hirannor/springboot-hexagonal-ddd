package io.github.hirannor.oms.application.usecase.payment;

import io.github.hirannor.oms.domain.order.command.InitializePayment;
import io.github.hirannor.oms.domain.order.command.PaymentInstruction;

public interface PaymentInitialization {
    PaymentInstruction initialize(final InitializePayment command);

}
