package hu.hirannor.hexagonal.adapter.web.rest.product;

import hu.hirannor.hexagonal.adapter.web.rest.products.model.CurrencyModel;
import hu.hirannor.hexagonal.domain.Currency;

import java.util.function.Function;

public class CurrencyModelToDomainMapper implements Function<CurrencyModel, Currency> {
    public CurrencyModelToDomainMapper() {}

    @Override
    public Currency apply(final CurrencyModel model) {
        if (model == null) return null;

        return switch (model) {
            case EUR -> Currency.EUR;
            case HUF -> Currency.HUF;
        };
    }
}
