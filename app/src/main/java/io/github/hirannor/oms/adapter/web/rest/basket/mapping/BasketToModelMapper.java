package io.github.hirannor.oms.adapter.web.rest.basket.mapping;

import io.github.hirannor.oms.adapter.web.rest.baskets.model.BasketModel;
import io.github.hirannor.oms.adapter.web.rest.baskets.model.MoneyModel;
import io.github.hirannor.oms.domain.basket.Basket;
import io.github.hirannor.oms.domain.core.valueobject.Money;

import java.util.function.Function;

public class BasketToModelMapper implements Function<Basket, BasketModel> {
    private final Function<Money, MoneyModel> mapMoneyToModel;

    public BasketToModelMapper() {
        this.mapMoneyToModel = new MoneyToModelMapper();
    }
    @Override
    public BasketModel apply(final Basket basket) {
        if (basket == null) return null;

        return new BasketModel()
            .id(basket.id().asText())
            .customerId(basket.customer().asText())
            .totalPrice(mapMoneyToModel.apply(basket.totalPrice()));
    }
}
