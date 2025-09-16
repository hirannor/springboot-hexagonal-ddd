package hu.hirannor.hexagonal.adapter.persistence.jpa.payment.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.payment.PaymentModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.payment.PaymentStatusModel;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.payment.Payment;
import hu.hirannor.hexagonal.domain.payment.PaymentBuilder;
import hu.hirannor.hexagonal.domain.payment.PaymentId;
import hu.hirannor.hexagonal.domain.payment.PaymentStatus;

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
