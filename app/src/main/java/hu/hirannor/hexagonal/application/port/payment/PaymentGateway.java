package hu.hirannor.hexagonal.application.port.payment;

import hu.hirannor.hexagonal.application.service.payment.ProcessPayment;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;

public interface PaymentGateway {
    PaymentInstruction initialize(ProcessPayment payment);
}
