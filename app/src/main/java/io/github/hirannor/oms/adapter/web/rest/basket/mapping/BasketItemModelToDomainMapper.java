package io.github.hirannor.oms.adapter.web.rest.basket.mapping;

import io.github.hirannor.oms.adapter.web.rest.baskets.model.BasketItemModel;
import io.github.hirannor.oms.adapter.web.rest.baskets.model.MoneyModel;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.product.ProductId;

import java.util.function.Function;

public class BasketItemModelToDomainMapper implements Function<BasketItemModel, BasketItem> {

    private final Function<MoneyModel, Money> mapModelToDomain;

    public BasketItemModelToDomainMapper() {
        this.mapModelToDomain = new MoneyModelToDomainMapper();
    }

    @Override
    public BasketItem apply(final BasketItemModel model) {
        if (model == null) return null;

        return BasketItem.create(
                ProductId.from(model.getProductId()),
                model.getQuantity(),
                mapModelToDomain.apply(model.getPrice())
        );
    }
}
