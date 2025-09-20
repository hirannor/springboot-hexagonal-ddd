package io.github.hirannor.oms.adapter.web.rest.basket.mapping;

import io.github.hirannor.oms.adapter.web.rest.baskets.model.BasketItemModel;
import io.github.hirannor.oms.adapter.web.rest.baskets.model.BasketModel;
import io.github.hirannor.oms.adapter.web.rest.baskets.model.MoneyModel;
import io.github.hirannor.oms.application.service.basket.BasketItemView;
import io.github.hirannor.oms.application.service.basket.BasketView;
import io.github.hirannor.oms.domain.core.valueobject.Money;

import java.util.List;
import java.util.function.Function;

public class BasketViewToModelMapper implements Function<BasketView, BasketModel> {
    private final Function<BasketItemView, BasketItemModel> mapViewToModel;
    private final Function<Money, MoneyModel> mapMoneyToModel;

    public BasketViewToModelMapper() {
        this.mapViewToModel = new BasketItemViewToModelMapper();
        this.mapMoneyToModel = new MoneyToModelMapper();
    }

    @Override
    public BasketModel apply(final BasketView view) {
        if (view == null) return null;

        final List<BasketItemModel> items = view.items()
                .stream()
                .map(mapViewToModel)
                .toList();

        return new BasketModel()
                .id(view.basketId().asText())
                .customerId(view.customerId().asText())
                .totalPrice(mapMoneyToModel.apply(view.totalPrice()))
                .items(items);
    }
}
