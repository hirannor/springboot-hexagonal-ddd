package io.github.hirannor.oms.adapter.web.rest.basket.mapping;

import io.github.hirannor.oms.adapter.web.rest.baskets.model.CurrencyModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;

import java.util.function.Function;

public class CurrencyToModelMapper implements Function<Currency, CurrencyModel> {
    public CurrencyToModelMapper() {
    }

    @Override
    public CurrencyModel apply(final Currency domain) {
        if (domain == null) return null;

        return switch (domain) {
            case EUR -> CurrencyModel.EUR;
            case HUF -> CurrencyModel.HUF;
        };
    }
}
