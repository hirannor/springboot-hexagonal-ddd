package hu.hirannor.hexagonal.application.usecase.order;

import hu.hirannor.hexagonal.domain.order.command.PayOrder;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;

public interface OrderPayment {
    PaymentInstruction pay(PayOrder order);
}
