package hu.hirannor.hexagonal.adapter.persistence.jpa.basket.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.basket.BasketItemModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.basket.BasketModel;
import hu.hirannor.hexagonal.domain.basket.*;
import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
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

        final List<BasketItem> basketItems = model.getItems()
                .stream()
                .map(mapModelToDomain)
                .toList();

        return Basket.from(
                BasketId.from(model.getBasketId()),
                CustomerId.from(model.getCustomerId()),
                basketItems
        );
    }
}
