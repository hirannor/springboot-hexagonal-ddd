package io.github.hirannor.oms.adapter.payment.mock;

import io.github.hirannor.oms.application.port.payment.PaymentGateway;
import io.github.hirannor.oms.application.port.payment.PaymentRequest;
import io.github.hirannor.oms.domain.order.command.PaymentInstruction;
import io.github.hirannor.oms.domain.payment.PaymentReceipt;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@DrivenAdapter
class MockPaymentGateway implements PaymentGateway {
    @Override
    public PaymentInstruction initialize(final PaymentRequest payment) {
        return null;
    }

    @Override
    public Optional<PaymentReceipt> processCallback(final String payload, final String signatureHeader) {
        return Optional.empty();
    }
}
