package io.github.hirannor.oms.adapter.payment.stripe.mapping;

import com.stripe.param.checkout.SessionCreateParams;
import io.github.hirannor.oms.domain.payment.PaymentMethod;

import java.util.function.Function;

public class PaymentMethodToTypeMapper implements Function<PaymentMethod, SessionCreateParams.PaymentMethodType> {

    @Override
    public SessionCreateParams.PaymentMethodType apply(final PaymentMethod domain) {
        if (domain == null) return null;

        return switch (domain) {
            case CARD -> SessionCreateParams.PaymentMethodType.CARD;
        };
    }
}
