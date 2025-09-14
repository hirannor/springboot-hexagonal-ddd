package hu.hirannor.hexagonal.application.usecase.order;

import hu.hirannor.hexagonal.domain.order.command.InitializePayment;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;

public interface OrderPaymentInitialization {
    PaymentInstruction initializeBy(InitializePayment order);
}
