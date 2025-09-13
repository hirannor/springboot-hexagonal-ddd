package hu.hirannor.hexagonal.adapter.payment.stripe;

import hu.hirannor.hexagonal.application.port.payment.PaymentGateway;
import hu.hirannor.hexagonal.application.service.payment.ProcessPayment;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
import org.springframework.stereotype.Component;

@Component
@DrivenAdapter
class StripePaymentGateway implements PaymentGateway {
    @Override
    public PaymentInstruction initialize(ProcessPayment payment) {
        return null;
    }
}
