package io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.currency.CurrencyModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.currency.CurrencyToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentSucceededModel;
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
        if (evt.orderId() == null) return null;

        return new PaymentSucceededModel(
                evt.id().asText(),
                evt.paymentId().asText(),
                evt.orderId().asText(),
                evt.money().amount(),
                mapCurrencyToModel.apply(evt.money().currency()).value()
        );
    }
}
