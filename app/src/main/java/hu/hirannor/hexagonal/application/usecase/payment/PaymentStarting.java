package hu.hirannor.hexagonal.application.usecase.payment;

import hu.hirannor.hexagonal.domain.order.command.InitializePayment;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;

public interface PaymentStarting {
    PaymentInstruction initialize(final InitializePayment command);

}
