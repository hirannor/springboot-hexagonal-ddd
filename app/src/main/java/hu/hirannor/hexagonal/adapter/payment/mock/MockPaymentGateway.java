package hu.hirannor.hexagonal.adapter.payment.mock;

import hu.hirannor.hexagonal.application.port.payment.PaymentGateway;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;

@DrivenAdapter
class MockPaymentGateway implements PaymentGateway {
    @Override
    public void pay() {

    }
}
