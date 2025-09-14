package hu.hirannor.hexagonal.adapter.web.rest.product.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.products.model.CurrencyModel;
import hu.hirannor.hexagonal.adapter.web.rest.products.model.MoneyModel;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.Money;

import java.util.function.Function;

public class MoneyModelToDomainMapper implements Function<MoneyModel, Money> {

    private final  Function<CurrencyModel, Currency> mapModelToDomain;

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
