package io.github.hirannor.oms.adapter.persistence.jpa.basket.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.basket.BasketItemModel;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.product.ProductId;

import java.util.function.Function;

public class BasketItemModelToDomainMapper implements Function<BasketItemModel, BasketItem> {

    private final Function<CurrencyModel, Currency> mapModelToDomain;

    public BasketItemModelToDomainMapper() {
        this.mapModelToDomain = new CurrencyModelToDomainMapper();
    }

    @Override
    public BasketItem apply(final BasketItemModel model) {
        if (model == null) return null;

        final Currency currency = mapModelToDomain.apply(model.getPriceCurrency());

        return BasketItem.create(
                ProductId.from(model.getProductId()),
                model.getQuantity(),
                Money.of(model.getPriceAmount(), currency)
        );
    }
}
