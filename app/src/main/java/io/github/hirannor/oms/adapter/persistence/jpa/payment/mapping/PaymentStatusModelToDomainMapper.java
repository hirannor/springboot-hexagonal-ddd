package io.github.hirannor.oms.adapter.persistence.jpa.payment.mapping;


import io.github.hirannor.oms.adapter.persistence.jpa.payment.PaymentStatusModel;
import io.github.hirannor.oms.domain.payment.PaymentStatus;

import java.util.function.Function;

public class PaymentStatusModelToDomainMapper implements Function<PaymentStatusModel, PaymentStatus> {
    public PaymentStatusModelToDomainMapper() {
    }

    @Override
    public PaymentStatus apply(final PaymentStatusModel model) {
        if (model == null) return null;

        return switch (model) {
            case SUCCEEDED -> PaymentStatus.SUCCEEDED;
            case INITIALIZED -> PaymentStatus.INITIALIZED;
            case EXPIRED -> PaymentStatus.EXPIRED;
            case FAILED -> PaymentStatus.FAILED;
            case CANCELED -> PaymentStatus.CANCELED;
        };
    }
}
