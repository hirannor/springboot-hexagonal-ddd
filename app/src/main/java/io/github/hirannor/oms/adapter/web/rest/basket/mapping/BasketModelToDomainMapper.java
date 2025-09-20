package io.github.hirannor.oms.adapter.web.rest.basket.mapping;

import io.github.hirannor.oms.adapter.web.rest.baskets.model.BasketItemModel;
import io.github.hirannor.oms.adapter.web.rest.baskets.model.BasketModel;
import io.github.hirannor.oms.domain.basket.Basket;
import io.github.hirannor.oms.domain.basket.BasketId;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;

import java.util.List;
import java.util.function.Function;

public class BasketModelToDomainMapper implements Function<BasketModel, Basket> {

    private final Function<BasketItemModel, BasketItem> mapModelToDomain;

    public BasketModelToDomainMapper() {
        this.mapModelToDomain = new BasketItemModelToDomainMapper();
    }

    @Override
    public Basket apply(final BasketModel model) {
        if (model == null) return null;

        final List<BasketItem> items = model.getItems()
                .stream()
                .map(mapModelToDomain)
                .toList();

        return Basket.from(
                BasketId.from(model.getId()),
                CustomerId.from(model.getCustomerId()),
                items
        );
    }
}
