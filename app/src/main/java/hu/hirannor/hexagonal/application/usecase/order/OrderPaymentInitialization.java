package hu.hirannor.hexagonal.application.usecase.order;

import hu.hirannor.hexagonal.domain.order.command.PayOrder;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;

public interface OrderPaymentInitialization {
    PaymentInstruction initPay(PayOrder order);
}
