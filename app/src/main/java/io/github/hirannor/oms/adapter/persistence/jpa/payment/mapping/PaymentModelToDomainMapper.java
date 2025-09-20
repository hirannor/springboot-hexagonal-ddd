package io.github.hirannor.oms.adapter.persistence.jpa.payment.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.payment.PaymentModel;
import io.github.hirannor.oms.adapter.persistence.jpa.payment.PaymentStatusModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.Payment;
import io.github.hirannor.oms.domain.payment.PaymentBuilder;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.domain.payment.PaymentStatus;

import java.util.function.Function;

public class PaymentModelToDomainMapper implements Function<PaymentModel, Payment> {

    private final Function<PaymentStatusModel, PaymentStatus> mapStatusModelToDomain;
    private final Function<CurrencyModel, Currency> mapCurrencyModelToDomain;

    public PaymentModelToDomainMapper() {
        this.mapStatusModelToDomain = new PaymentStatusModelToDomainMapper();
        this.mapCurrencyModelToDomain = new CurrencyModelToDomainMapper();
    }

    @Override
    public Payment apply(final PaymentModel model) {
        if (model == null) return null;

        final PaymentStatus status = mapStatusModelToDomain.apply(model.getStatus());
        final Money amount = Money.of(
                model.getAmount(),
                mapCurrencyModelToDomain.apply(model.getCurrency())
        );

        return new PaymentBuilder()
                .id(PaymentId.from(model.getPaymentId()))
                .order(OrderId.from(model.getOrderId()))
                .amount(amount)
                .status(status)
                .providerReference(model.getProviderReference())
                .assemble();
    }
}
