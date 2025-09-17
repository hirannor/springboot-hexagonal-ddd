package hu.hirannor.hexagonal.adapter.persistence.jpa.basket.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyToModelMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.basket.BasketItemModel;
import hu.hirannor.hexagonal.domain.core.valueobject.Currency;
import hu.hirannor.hexagonal.domain.basket.BasketItem;

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
