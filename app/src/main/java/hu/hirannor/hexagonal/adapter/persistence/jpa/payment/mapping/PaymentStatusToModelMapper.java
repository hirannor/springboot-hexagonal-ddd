package hu.hirannor.hexagonal.adapter.persistence.jpa.payment.mapping;


import hu.hirannor.hexagonal.adapter.persistence.jpa.payment.PaymentStatusModel;
import hu.hirannor.hexagonal.domain.payment.PaymentStatus;
import java.util.function.Function;

public class PaymentStatusToModelMapper implements Function<PaymentStatus, PaymentStatusModel> {
    public PaymentStatusToModelMapper() {}

    @Override
    public PaymentStatusModel apply(final PaymentStatus domain) {
        if (domain == null) return null;

        return switch (domain) {
            case SUCCEEDED -> PaymentStatusModel.SUCCEEDED;
            case INITIALIZED -> PaymentStatusModel.INITIALIZED;
            case FAILED -> PaymentStatusModel.FAILED;
            case CANCELED -> PaymentStatusModel.CANCELED;
        };
    }
}
