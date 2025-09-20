package io.github.hirannor.oms.adapter.web.rest.basket.mapping;

import io.github.hirannor.oms.adapter.web.rest.baskets.model.CurrencyModel;
import io.github.hirannor.oms.adapter.web.rest.baskets.model.MoneyModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.core.valueobject.Money;

import java.util.function.Function;

public class MoneyModelToDomainMapper implements Function<MoneyModel, Money> {

    private final Function<CurrencyModel, Currency> mapModelToDomain;

    public MoneyModelToDomainMapper() {
        this.mapModelToDomain = new CurrencyModelToDomainMapper();
    }

    @Override
    public Money apply(final MoneyModel model) {
        if (model == null) return null;

        final Currency currency = mapModelToDomain.apply(model.getCurrency());
        return Money.of(model.getAmount(), currency);
    }
}
