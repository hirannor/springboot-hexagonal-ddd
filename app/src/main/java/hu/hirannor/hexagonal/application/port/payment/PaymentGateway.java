package hu.hirannor.hexagonal.application.port.payment;

import hu.hirannor.hexagonal.application.service.payment.PaymentReceipt;
import hu.hirannor.hexagonal.application.service.payment.ProcessPayment;

public interface PaymentGateway {
    PaymentReceipt process(ProcessPayment payment);
}
