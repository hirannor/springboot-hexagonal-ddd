package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.basket.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.basket.BasketItemModel;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.core.valueobject.Currency;

import java.util.function.Function;

public class BasketItemToModelMapper implements Function<BasketItem, BasketItemModel> {

    private final Function<Currency, CurrencyModel> mapCurrencyToModel;

    public BasketItemToModelMapper() {
        this.mapCurrencyToModel = new CurrencyToModelMapper();
    }

    @Override
    public BasketItemModel apply(final BasketItem item) {
        if (item == null) return null;

        return new BasketItemModel(
                item.productId().asText(),
                item.quantity(),
                item.price().amount(),
                mapCurrencyToModel.apply(item.price().currency())
        );
    }
}
