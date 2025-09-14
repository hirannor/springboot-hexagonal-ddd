package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyToModelMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.PaymentTransactionModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.PaymentMethodModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.PaymentStatusModel;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.order.payment.PaymentMethod;
import hu.hirannor.hexagonal.domain.order.payment.PaymentStatus;
import hu.hirannor.hexagonal.domain.order.payment.PaymentTransaction;

import java.util.function.Function;

public class PaymentTransactionToModelMapper implements Function<PaymentTransaction, PaymentTransactionModel> {

    private final Function<PaymentStatus, PaymentStatusModel> mapPaymentStatusToModel;
    private final Function<Currency, CurrencyModel> mapCurrencyToModel;
    private final Function<PaymentMethod, PaymentMethodModel> mapPaymentMethodToModel;

    public PaymentTransactionToModelMapper() {
        this.mapPaymentStatusToModel = new PaymentStatusToModelMapper();
        this.mapCurrencyToModel = new CurrencyToModelMapper();
        this.mapPaymentMethodToModel = new PaymentMethodToModelMapper();
    }

    @Override
    public PaymentTransactionModel apply(final PaymentTransaction domain) {
        if (domain == null) return null;

        final PaymentTransactionModel model = new PaymentTransactionModel();
        model.setTransactionId(domain.transactionId());
        model.setPaymentMethod(mapPaymentMethodToModel.apply(domain.paymentMethod()));
        model.setCreatedAt(domain.createdAt());
        model.setAmount(domain.amount().amount());
        model.setStatus(mapPaymentStatusToModel.apply(domain.status()));
        model.setCurrency(mapCurrencyToModel.apply(domain.amount().currency()));

        return model;
    }
}
