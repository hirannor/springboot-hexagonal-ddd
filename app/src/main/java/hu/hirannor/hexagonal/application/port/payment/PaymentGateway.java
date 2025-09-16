package hu.hirannor.hexagonal.application.port.payment;

import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;
import hu.hirannor.hexagonal.domain.payment.PaymentReceipt;

import java.util.Optional;

public interface PaymentGateway {
    PaymentInstruction initialize(PaymentRequest payment);

    Optional<PaymentReceipt> processCallback(String payload, String signatureHeader);

}
