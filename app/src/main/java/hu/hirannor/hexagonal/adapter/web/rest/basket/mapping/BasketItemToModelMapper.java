package hu.hirannor.hexagonal.adapter.web.rest.basket.mapping;


import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.BasketItemModel;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.MoneyModel;
import hu.hirannor.hexagonal.domain.basket.BasketItem;
import hu.hirannor.hexagonal.domain.core.valueobject.Money;

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
