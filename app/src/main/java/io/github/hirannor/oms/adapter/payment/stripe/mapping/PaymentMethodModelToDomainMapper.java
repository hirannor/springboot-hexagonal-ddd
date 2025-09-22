package io.github.hirannor.oms.adapter.payment.stripe.mapping;

import io.github.hirannor.oms.adapter.payment.stripe.PaymentMethodModel;
import io.github.hirannor.oms.domain.payment.PaymentMethod;

import java.util.function.Function;

public class PaymentMethodModelToDomainMapper implements Function<PaymentMethodModel, PaymentMethod> {

    public PaymentMethodModelToDomainMapper() {
    }

    @Override
    public PaymentMethod apply(final PaymentMethodModel method) {
        if (method == null) return null;

        return switch (method) {
            case CARD -> PaymentMethod.CARD;
        };
    }
}
