package io.github.hirannor.oms.adapter.web.rest.basket.mapping;

import io.github.hirannor.oms.adapter.web.rest.baskets.model.CurrencyModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;

import java.util.function.Function;

public class CurrencyModelToDomainMapper implements Function<CurrencyModel, Currency> {
    public CurrencyModelToDomainMapper() {
    }

    @Override
    public Currency apply(final CurrencyModel model) {
        if (model == null) return null;

        return switch (model) {
            case EUR -> Currency.EUR;
            case HUF -> Currency.HUF;
        };
    }
}
