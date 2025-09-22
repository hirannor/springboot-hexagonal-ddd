package io.github.hirannor.oms.adapter.web.rest.basket.mapping;


import io.github.hirannor.oms.adapter.web.rest.baskets.model.BasketItemModel;
import io.github.hirannor.oms.adapter.web.rest.baskets.model.MoneyModel;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.core.valueobject.Money;

import java.util.function.Function;

public class BasketItemToModelMapper implements Function<BasketItem, BasketItemModel> {

    private final Function<Money, MoneyModel> mapDomainToModel;

    public BasketItemToModelMapper() {
        this.mapDomainToModel = new MoneyToModelMapper();
    }

    @Override
    public BasketItemModel apply(final BasketItem domain) {
        if (domain == null) return null;
        return new BasketItemModel()
                .price(mapDomainToModel.apply(domain.price()))
                .productId(domain.productId().asText())
                .quantity(domain.quantity());
    }
}