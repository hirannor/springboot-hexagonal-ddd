package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.currency.CurrencyModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.currency.CurrencyModelToDomainMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment.PaymentSucceededModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.domain.payment.events.PaymentSucceeded;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PaymentSucceededModelToDomainMapper implements MessageModelMapper<PaymentSucceededModel, PaymentSucceeded> {
    private final Function<CurrencyModel, Currency> mapCurrencyModelToDomain;

    public PaymentSucceededModelToDomainMapper() {
        this.mapCurrencyModelToDomain = new CurrencyModelToDomainMapper();
    }

    @Override
    public PaymentSucceeded apply(final PaymentSucceededModel model) {
        if (model == null) return null;

        return PaymentSucceeded.recreate(
                model.id(),
                PaymentId.from(model.paymentId()),
                OrderId.from(model.orderId()),
                Money.of(model.amount(), mapCurrencyModelToDomain.apply(CurrencyModel.from(model.currency())))
        );
    }

    @Override
    public Class<PaymentSucceededModel> messageType() {
        return PaymentSucceededModel.class;
    }
}
