package hu.hirannor.hexagonal.adapter.payment.stripe.mapping;

import hu.hirannor.hexagonal.adapter.payment.stripe.PaymentMethodModel;
import hu.hirannor.hexagonal.domain.payment.PaymentMethod;
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
