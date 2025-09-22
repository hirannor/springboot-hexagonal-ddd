package io.github.hirannor.oms.adapter.web.rest.basket.mapping;


import io.github.hirannor.oms.adapter.web.rest.baskets.model.BasketItemModel;
import io.github.hirannor.oms.adapter.web.rest.baskets.model.MoneyModel;
import io.github.hirannor.oms.application.service.basket.BasketItemView;
import io.github.hirannor.oms.domain.core.valueobject.Money;

import java.util.function.Function;

public class BasketItemViewToModelMapper implements Function<BasketItemView, BasketItemModel> {

    private final Function<Money, MoneyModel> mapDomainToModel;

    public BasketItemViewToModelMapper() {
        this.mapDomainToModel = new MoneyToModelMapper();
    }

    @Override
    public BasketItemModel apply(final BasketItemView view) {
        if (view == null) return null;

        return new BasketItemModel()
                .price(mapDomainToModel.apply(view.price()))
                .name(view.name())
                .description(view.description())
                .productId(view.productId().asText())
                .quantity(view.quantity());
    }
}
