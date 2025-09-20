package io.github.hirannor.oms.adapter.web.rest.basket.mapping;

import io.github.hirannor.oms.adapter.web.rest.baskets.model.CurrencyModel;
import io.github.hirannor.oms.adapter.web.rest.baskets.model.MoneyModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.core.valueobject.Money;

import java.util.function.Function;

public class MoneyToModelMapper implements Function<Money, MoneyModel> {

    private final Function<Currency, CurrencyModel> mapDomainToModel;

    public MoneyToModelMapper() {
        this.mapDomainToModel = new CurrencyToModelMapper();
    }

    @Override
    public MoneyModel apply(final Money domain) {
        if (domain == null) return null;

        return new MoneyModel()
                .amount(domain.amount())
                .currency(mapDomainToModel.apply(domain.currency()));

    }
}
