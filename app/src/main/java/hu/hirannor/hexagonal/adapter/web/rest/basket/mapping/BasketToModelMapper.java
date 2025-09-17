package hu.hirannor.hexagonal.adapter.web.rest.basket.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.BasketItemModel;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.BasketModel;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.MoneyModel;
import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.basket.BasketItem;
import hu.hirannor.hexagonal.domain.core.valueobject.Money;

import java.util.List;
import java.util.function.Function;

public class BasketToModelMapper implements Function<Basket, BasketModel> {
    private final Function<BasketItem, BasketItemModel> mapDomainToModel;
    private final Function<Money, MoneyModel> mapMoneyToModel;

    public BasketToModelMapper() {
        this.mapDomainToModel = new BasketItemToModelMapper();
        this.mapMoneyToModel = new MoneyToModelMapper();
    }

    @Override
    public BasketModel apply(final Basket domain) {
        if (domain == null) return null;

        final List<BasketItemModel> items = domain.items()
                .stream()
                .map(mapDomainToModel)
                .toList();

        return  new BasketModel()
                .id(domain.id().asText())
                .customerId(domain.customer().asText())
                .totalPrice(mapMoneyToModel.apply(domain.totalPrice()))
                .items(items);
    }
}
