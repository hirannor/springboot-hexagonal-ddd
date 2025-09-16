package hu.hirannor.hexagonal.adapter.persistence.jpa.payment.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyToModelMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.payment.PaymentModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.payment.PaymentStatusModel;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.payment.Payment;
import hu.hirannor.hexagonal.domain.payment.PaymentStatus;
import hu.hirannor.hexagonal.infrastructure.modelling.Modeller;

import java.util.function.Function;

public class PaymentModeller implements Modeller<PaymentModel> {

    private final Function<Currency, CurrencyModel> mapCurrencyToModel;
    private final Function<PaymentStatus, PaymentStatusModel> mapStatusToModel;

    private final Payment domain;

    public PaymentModeller(final Payment domain) {
        this.domain = domain;
        this.mapCurrencyToModel = new CurrencyToModelMapper();
        this.mapStatusToModel = new PaymentStatusToModelMapper();
    }

    public static PaymentModeller applyChangesFrom(final Payment domain) {
        return new PaymentModeller(domain);
    }

    @Override
    public PaymentModel to(final PaymentModel from) {
        if (from == null) return null;

        from.setPaymentId(domain.id().asText());
        from.setOrderId(domain.orderId().asText());
        from.setAmount(domain.amount().amount());
        from.setCurrency(mapCurrencyToModel.apply(domain.amount().currency()));
        from.setStatus(mapStatusToModel.apply(domain.status()));
        from.setProviderReference(domain.providerReference());
        from.setCreatedAt(domain.createdAt());

        return from;
    }
}
