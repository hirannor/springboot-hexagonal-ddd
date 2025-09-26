package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.payment.PaymentSucceededModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.payment.events.PaymentSucceeded;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component(value = "PaymentSucceededToModelMapper")
public class PaymentSucceededToModelMapper implements MessageMapper<PaymentSucceeded, PaymentSucceededModel> {

    private final Function<Currency, CurrencyModel> mapCurrencyToModel;

    public PaymentSucceededToModelMapper() {
        this.mapCurrencyToModel = new CurrencyToModelMapper();
    }

    @Override
    public PaymentSucceededModel apply(final PaymentSucceeded evt) {
        if (evt == null) return null;

        return new PaymentSucceededModel(
                evt.id(),
                evt.paymentId().asText(),
                evt.orderId().asText(),
                evt.money().amount(),
                mapCurrencyToModel.apply(evt.money().currency()).dbRepresentation()
        );
    }

    @Override
    public Class<PaymentSucceeded> messageType() {
        return PaymentSucceeded.class;
    }
}
