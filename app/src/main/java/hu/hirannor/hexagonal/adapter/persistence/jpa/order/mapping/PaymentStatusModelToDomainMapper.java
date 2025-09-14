package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.order.PaymentStatusModel;
import hu.hirannor.hexagonal.domain.order.payment.PaymentStatus;

import java.util.function.Function;

public class PaymentStatusModelToDomainMapper implements Function<PaymentStatusModel, PaymentStatus> {
    public PaymentStatusModelToDomainMapper() {}

    @Override
    public PaymentStatus apply(final PaymentStatusModel model) {
        if (model == null) return null;

        return switch (model) {
            case SUCCESS -> PaymentStatus.SUCCESS;
            case PENDING -> PaymentStatus.PENDING;
            case FAILURE -> PaymentStatus.FAILURE;
            case CANCELLED -> PaymentStatus.CANCELLED;
        };
    }
}
