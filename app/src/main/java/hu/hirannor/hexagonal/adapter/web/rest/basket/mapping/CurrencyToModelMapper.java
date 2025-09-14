package hu.hirannor.hexagonal.adapter.web.rest.basket.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.CurrencyModel;
import hu.hirannor.hexagonal.domain.Currency;

import java.util.function.Function;

public class CurrencyToModelMapper implements Function<Currency, CurrencyModel> {
    public CurrencyToModelMapper() {}

    @Override
    public CurrencyModel apply(final Currency domain) {
        if (domain == null) return null;

        return switch (domain) {
            case EUR -> CurrencyModel.EUR;
            case HUF -> CurrencyModel.HUF;
        };
    }
}
