package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.PaymentTransactionModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.PaymentMethodModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.PaymentStatusModel;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.payment.PaymentMethod;
import hu.hirannor.hexagonal.domain.order.payment.PaymentStatus;
import hu.hirannor.hexagonal.domain.order.payment.PaymentTransaction;

import java.util.function.Function;

public class PaymentTransactionModelToDomainMapper implements Function<PaymentTransactionModel, PaymentTransaction> {

    private final Function<PaymentStatusModel, PaymentStatus> mapPaymentStatusModelToDomain;
    private final Function<CurrencyModel, Currency> mapCurrencyModelToDomain;
    private final Function<PaymentMethodModel, PaymentMethod> mapPaymentMethodModelToDomain;

    public PaymentTransactionModelToDomainMapper() {
        this.mapPaymentStatusModelToDomain = new PaymentStatusModelToDomainMapper();
        this.mapCurrencyModelToDomain = new CurrencyModelToDomainMapper();
        this.mapPaymentMethodModelToDomain = new PaymentMethodModelToDomainMapper();
    }

    @Override
    public PaymentTransaction apply(final PaymentTransactionModel model) {
        if (model == null) return null;

        return PaymentTransaction.create(
                model.getTransactionId(),
                model.getProviderPaymentId(),
                mapPaymentMethodModelToDomain.apply(model.getPaymentMethod()),
                OrderId.from(model.getOrder().getOrderId()),
                mapPaymentStatusModelToDomain.apply(model.getStatus()),
                Money.of(model.getAmount(), mapCurrencyModelToDomain.apply(model.getCurrency()))
        );
    }
}
