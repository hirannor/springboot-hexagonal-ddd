package hu.hirannor.hexagonal.adapter.persistence.jpa.payment.mapping;


import hu.hirannor.hexagonal.adapter.persistence.jpa.payment.PaymentStatusModel;
import hu.hirannor.hexagonal.domain.payment.PaymentStatus;

import java.util.function.Function;

public class PaymentStatusModelToDomainMapper implements Function<PaymentStatusModel, PaymentStatus> {
    public PaymentStatusModelToDomainMapper() {}

    @Override
    public PaymentStatus apply(final PaymentStatusModel model) {
        if (model == null) return null;

        return switch (model) {
            case SUCCEEDED -> PaymentStatus.SUCCEEDED;
            case INITIALIZED -> PaymentStatus.INITIALIZED;
            case FAILED -> PaymentStatus.FAILED;
            case CANCELED -> PaymentStatus.CANCELED;
        };
    }
}
