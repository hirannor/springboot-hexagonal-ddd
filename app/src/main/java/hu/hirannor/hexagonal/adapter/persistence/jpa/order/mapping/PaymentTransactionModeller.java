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
import hu.hirannor.hexagonal.infrastructure.modelling.Modeller;

import java.util.function.Function;

public class PaymentTransactionModeller implements Modeller<PaymentTransactionModel> {

    private final Function<PaymentStatus, PaymentStatusModel> mapPaymentStatusToModel;
    private final Function<Currency, CurrencyModel> mapCurrencyToModel;
    private final Function<PaymentMethod, PaymentMethodModel> mapPaymentMethodToModel;

    private final PaymentTransaction domain;

    public PaymentTransactionModeller(final PaymentTransaction domain) {
        this.domain = domain;
        this.mapPaymentStatusToModel = new PaymentStatusToModelMapper();
        this.mapCurrencyToModel = new CurrencyToModelMapper();
        this.mapPaymentMethodToModel = new PaymentMethodToModelMapper();
    }

    public static PaymentTransactionModeller applyChangesFrom(final PaymentTransaction domain) {
        return new PaymentTransactionModeller(domain);
    }

    public PaymentTransactionModel to(final PaymentTransactionModel from) {
        final PaymentTransactionModel model = (from != null) ? from : new PaymentTransactionModel();

        model.setTransactionId(domain.transactionId());
        model.setProviderPaymentId(domain.providerPaymentId());
        model.setPaymentMethod(mapPaymentMethodToModel.apply(domain.paymentMethod()));
        model.setCreatedAt(domain.createdAt());
        model.setAmount(domain.amount().amount());
        model.setStatus(mapPaymentStatusToModel.apply(domain.status()));
        model.setCurrency(mapCurrencyToModel.apply(domain.amount().currency()));

        return model;
    }
}
