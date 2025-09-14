package hu.hirannor.hexagonal.application.port.payment;

import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;
import hu.hirannor.hexagonal.domain.order.payment.PaymentReceipt;

public interface PaymentGateway {
    PaymentInstruction initialize(PaymentRequest payment);

    PaymentReceipt processCallback(String payload, String signatureHeader);

}
