package hu.hirannor.hexagonal.adapter.persistence.jpa;

import hu.hirannor.hexagonal.domain.Currency;

import java.util.function.Function;

public class CurrencyToModelMapper implements Function<Currency, CurrencyModel> {
    public CurrencyToModelMapper() {}

    @Override
    public CurrencyModel apply(final Currency model) {
        if (model == null) return null;

        return switch (model) {
            case EUR -> CurrencyModel.EUR;
            case HUF -> CurrencyModel.HUF;
        };
    }
}
