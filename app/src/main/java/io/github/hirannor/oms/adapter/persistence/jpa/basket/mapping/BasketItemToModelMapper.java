package io.github.hirannor.oms.adapter.persistence.jpa.basket.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.basket.BasketItemModel;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.core.valueobject.Currency;

import java.util.function.Function;

public class BasketItemToModelMapper implements Function<BasketItem, BasketItemModel> {

    private final Function<Currency, CurrencyModel> mapCurrencyToModel;

    public BasketItemToModelMapper() {
        this.mapCurrencyToModel = new CurrencyToModelMapper();
    }


    @Override
    public BasketItemModel apply(final BasketItem domain) {
        if (domain == null) return null;

        final BasketItemModel model = new BasketItemModel();

        final CurrencyModel currency = mapCurrencyToModel.apply(domain.price().currency());

        model.setProductId(domain.productId().asText());
        model.setQuantity(domain.quantity());
        model.setPriceAmount(domain.price().amount());
        model.setPriceCurrency(currency);
        return model;
    }
}
