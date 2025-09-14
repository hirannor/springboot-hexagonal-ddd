package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.order.PaymentStatusModel;
import hu.hirannor.hexagonal.domain.order.payment.PaymentStatus;

import java.util.function.Function;

public class PaymentStatusToModelMapper implements Function<PaymentStatus, PaymentStatusModel> {
    public PaymentStatusToModelMapper() {}

    @Override
    public PaymentStatusModel apply(final PaymentStatus domain) {
        if (domain == null) return null;

        return switch (domain) {
            case PENDING -> PaymentStatusModel.PENDING;
            case SUCCESS -> PaymentStatusModel.SUCCESS;
            case FAILURE -> PaymentStatusModel.FAILURE;
            case CANCELLED -> PaymentStatusModel.CANCELLED;
        };
    }
}
