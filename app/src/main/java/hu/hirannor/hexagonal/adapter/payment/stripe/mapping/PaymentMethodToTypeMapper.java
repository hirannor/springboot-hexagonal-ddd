package hu.hirannor.hexagonal.adapter.payment.stripe.mapping;

import com.stripe.param.checkout.SessionCreateParams;
import hu.hirannor.hexagonal.domain.order.payment.PaymentMethod;

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
