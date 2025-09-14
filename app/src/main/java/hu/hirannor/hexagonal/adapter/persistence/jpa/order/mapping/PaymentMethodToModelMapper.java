package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.order.PaymentMethodModel;
import hu.hirannor.hexagonal.domain.order.payment.PaymentMethod;

import java.util.function.Function;

public class PaymentMethodToModelMapper implements Function<PaymentMethod, PaymentMethodModel> {

    public PaymentMethodToModelMapper() {}

    @Override
    public PaymentMethodModel apply(final PaymentMethod method) {
        if (method == null) return null;

        return switch (method) {
            case CARD -> PaymentMethodModel.CARD;
        };
    }
}
