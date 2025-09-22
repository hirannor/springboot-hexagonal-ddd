package io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentSucceededModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.payment.events.PaymentSucceeded;

import java.util.function.Function;

public class PaymentSucceededToModelMapper implements Function<PaymentSucceeded, PaymentSucceededModel> {

    private final Function<Currency, CurrencyModel> mapCurrencyToModel;


    public PaymentSucceededToModelMapper() {
        this.mapCurrencyToModel = new CurrencyToModelMapper();
    }

    @Override
    public PaymentSucceededModel apply(final PaymentSucceeded evt) {
        if (evt == null) return null;

        return new PaymentSucceededModel(
                evt.id().asText(),
                evt.paymentId().asText(),
                evt.orderId().asText(),
                evt.money().amount(),
                mapCurrencyToModel.apply(evt.money().currency()).dbRepresentation()
        );
    }
}
