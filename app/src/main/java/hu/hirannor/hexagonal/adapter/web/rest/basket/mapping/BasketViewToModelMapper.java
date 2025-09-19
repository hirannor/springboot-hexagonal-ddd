package hu.hirannor.hexagonal.adapter.web.rest.basket.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.*;
import hu.hirannor.hexagonal.application.service.basket.BasketItemView;
import hu.hirannor.hexagonal.application.service.basket.BasketView;
import hu.hirannor.hexagonal.domain.core.valueobject.Money;
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
