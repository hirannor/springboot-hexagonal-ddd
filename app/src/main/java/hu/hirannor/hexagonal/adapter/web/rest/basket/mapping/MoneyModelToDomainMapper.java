package hu.hirannor.hexagonal.adapter.web.rest.basket.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.CurrencyModel;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.MoneyModel;
import hu.hirannor.hexagonal.domain.core.valueobject.Currency;
import hu.hirannor.hexagonal.domain.core.valueobject.Money;
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
