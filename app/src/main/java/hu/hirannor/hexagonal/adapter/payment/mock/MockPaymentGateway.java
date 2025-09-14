package hu.hirannor.hexagonal.adapter.payment.mock;

import hu.hirannor.hexagonal.application.port.payment.PaymentGateway;
import hu.hirannor.hexagonal.application.port.payment.PaymentRequest;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;
import hu.hirannor.hexagonal.domain.order.payment.PaymentReceipt;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
import org.springframework.stereotype.Component;

@Component
@DrivenAdapter
class MockPaymentGateway implements PaymentGateway {
    @Override
    public PaymentInstruction initialize(final PaymentRequest payment) {
        return null;
    }

    @Override
    public PaymentReceipt processCallback(final String payload, final String signatureHeader) {
        return null;
    }
}
