package io.github.hirannor.oms.adapter.persistence.jpa.payment.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.payment.PaymentModel;
import io.github.hirannor.oms.adapter.persistence.jpa.payment.PaymentStatusModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.payment.Payment;
import io.github.hirannor.oms.domain.payment.PaymentStatus;
import io.github.hirannor.oms.infrastructure.modelling.Modeller;

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
