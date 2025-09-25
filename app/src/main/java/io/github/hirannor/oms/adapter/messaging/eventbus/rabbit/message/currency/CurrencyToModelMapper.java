package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.currency;


import io.github.hirannor.oms.domain.core.valueobject.Currency;

import java.util.function.Function;

public class CurrencyToModelMapper implements Function<Currency, CurrencyModel> {

    public CurrencyToModelMapper() {
    }

    @Override
    public CurrencyModel apply(final Currency currency) {
        if (currency == null) return null;

        return switch (currency) {
            case EUR -> CurrencyModel.EUR;
            case HUF -> CurrencyModel.HUF;
        };
    }
}
