package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.currency.CurrencyModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.currency.CurrencyToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.PaymentSucceededModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.payment.events.PaymentSucceeded;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PaymentSucceededToModelMapper implements MessageMapper<PaymentSucceeded, PaymentSucceededModel> {

    private final Function<Currency, CurrencyModel> mapCurrencyToModel;


    public PaymentSucceededToModelMapper() {
        this.mapCurrencyToModel = new CurrencyToModelMapper();
    }

    @Override
    public PaymentSucceededModel apply(final PaymentSucceeded evt) {
        if (evt.orderId() == null) return null;

        return new PaymentSucceededModel(
                evt.id(),
                evt.paymentId().asText(),
                evt.orderId().asText(),
                evt.money().amount(),
                mapCurrencyToModel.apply(evt.money().currency()).value()
        );
    }

    @Override
    public Class<PaymentSucceeded> messageType() {
        return PaymentSucceeded.class;
    }
}
