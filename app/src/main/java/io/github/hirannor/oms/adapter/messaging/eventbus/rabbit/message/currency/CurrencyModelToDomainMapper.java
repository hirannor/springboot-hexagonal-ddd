package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.currency;


import io.github.hirannor.oms.domain.core.valueobject.Currency;

import java.util.function.Function;

public class CurrencyModelToDomainMapper implements Function<CurrencyModel, Currency> {

    public CurrencyModelToDomainMapper() {
    }

    @Override
    public Currency apply(final CurrencyModel currency) {
        if (currency == null) return null;

        return switch (currency) {
            case EUR -> Currency.EUR;
            case HUF -> Currency.HUF;
        };
    }
}
