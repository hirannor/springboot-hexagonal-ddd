package hu.hirannor.hexagonal.adapter.persistence.jpa.basket.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.basket.BasketItemModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.basket.BasketModel;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.basket.BasketId;
import hu.hirannor.hexagonal.domain.basket.BasketItem;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BasketModelToDomainMapper implements Function<BasketModel, Basket> {

    private final Function<BasketItemModel, BasketItem> mapModelToDomain;

    public BasketModelToDomainMapper() {
        this.mapModelToDomain = new BasketItemModelToDomainMapper();
    }

    @Override
    public Basket apply(final BasketModel model) {
        if (model == null) return null;

        final Set<BasketItem> basketItems = model.getItems()
                .stream()
                .map(mapModelToDomain)
                .collect(Collectors.toSet());

        return Basket.from(
                BasketId.from(model.getBasketId()),
                CustomerId.from(model.getCustomerId()),
                basketItems
        );
    }
}
