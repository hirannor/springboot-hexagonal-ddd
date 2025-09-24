package io.github.hirannor.oms.adapter.persistence.jpa.basket.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.basket.BasketItemModel;
import io.github.hirannor.oms.adapter.persistence.jpa.basket.BasketModel;
import io.github.hirannor.oms.adapter.persistence.jpa.basket.BasketStatusModel;
import io.github.hirannor.oms.domain.basket.Basket;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.basket.BasketStatus;
import io.github.hirannor.oms.infrastructure.modelling.Modeller;

import java.util.function.Function;

public class BasketModeller implements Modeller<BasketModel> {

    private final Function<BasketItem, BasketItemModel> mapItemToModel;
    private final Function<BasketStatus, BasketStatusModel> mapStatusToModel;

    private final Basket domain;

    public BasketModeller(final Basket domain) {
        this.domain = domain;
        this.mapItemToModel = new BasketItemToModelMapper();
        this.mapStatusToModel = new BasketStatusToModelMapper();
    }

    public static BasketModeller applyChangesFrom(final Basket domain) {
        return new BasketModeller(domain);
    }

    @Override
    public BasketModel to(final BasketModel from) {
        if (from == null) return null;

        from.setBasketId(domain.id().asText());
        from.setCustomerId(domain.customer().asText());
        from.setStatus(mapStatusToModel.apply(domain.status()));

        from.getItems().clear();

        domain.items()
                .stream()
                .map(mapItemToModel)
                .forEach(from::addItem);

        return from;
    }
}
