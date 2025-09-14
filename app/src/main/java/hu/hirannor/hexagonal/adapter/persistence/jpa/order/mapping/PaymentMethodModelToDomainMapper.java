package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.order.PaymentMethodModel;
import hu.hirannor.hexagonal.domain.order.payment.PaymentMethod;

import java.util.function.Function;

public class PaymentMethodModelToDomainMapper implements Function<PaymentMethodModel, PaymentMethod> {

    public PaymentMethodModelToDomainMapper() {}

    @Override
    public PaymentMethod apply(final PaymentMethodModel method) {
        if (method == null) return null;

        return switch (method) {
            case CARD -> PaymentMethod.CARD;
        };
    }
}
