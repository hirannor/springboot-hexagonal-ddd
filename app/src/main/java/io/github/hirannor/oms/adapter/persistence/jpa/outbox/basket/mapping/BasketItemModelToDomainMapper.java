package io.github.hirannor.oms.adapter.persistence.jpa.outbox.basket.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.basket.BasketItemModel;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.product.ProductId;

import java.util.function.Function;

public class BasketItemModelToDomainMapper implements Function<BasketItemModel, BasketItem> {

    private final Function<CurrencyModel, Currency> mapCurrencyModelToDomain;

    public BasketItemModelToDomainMapper() {
        this.mapCurrencyModelToDomain = new CurrencyModelToDomainMapper();
    }

    @Override
    public BasketItem apply(final BasketItemModel model) {
        if (model == null) return null;


        return BasketItem.create(
                ProductId.from(model.getProductId()),
                model.getQuantity(),
                Money.of(model.getPriceAmount(), mapCurrencyModelToDomain.apply(model.getCurrency()))
        );
    }
}
