package io.github.hirannor.oms.application.port.payment;

import io.github.hirannor.oms.domain.order.command.PaymentInstruction;
import io.github.hirannor.oms.domain.payment.PaymentReceipt;

import java.util.Optional;

public interface PaymentGateway {
    PaymentInstruction initialize(PaymentRequest payment);

    Optional<PaymentReceipt> processCallback(String payload, String signatureHeader);

}
