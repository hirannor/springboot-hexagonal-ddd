package hu.hirannor.hexagonal.adapter.web.rest.basket.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.BasketModel;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.MoneyModel;
import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.core.valueobject.Money;
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
