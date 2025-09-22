package io.github.hirannor.oms.adapter.persistence.jpa.basket.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.basket.BasketItemModel;
import io.github.hirannor.oms.adapter.persistence.jpa.basket.BasketModel;
import io.github.hirannor.oms.domain.basket.Basket;
import io.github.hirannor.oms.domain.basket.BasketItem;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BasketToModelMapper implements Function<Basket, BasketModel> {

    private final Function<BasketItem, BasketItemModel> mapDomainToModel;

    public BasketToModelMapper() {
        this.mapDomainToModel = new BasketItemToModelMapper();
    }

    @Override
    public BasketModel apply(final Basket domain) {
        if (domain == null) return null;

        final BasketModel model = new BasketModel();
        model.setBasketId(domain.id().asText());
        model.setCustomerId(domain.customer().asText());

        final Set<BasketItemModel> basketItems = domain.items()
                .stream()
                .map(mapDomainToModel)
                .collect(Collectors.toSet());

        model.setItems(basketItems);

        return model;
    }
}
