package hu.hirannor.hexagonal.adapter.persistence.jpa.basket.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.basket.BasketItemModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.basket.BasketModel;
import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.basket.BasketItem;
import hu.hirannor.hexagonal.infrastructure.modelling.Modeller;

import java.util.function.Function;

public class BasketModeller implements Modeller<BasketModel> {

    private final Function<BasketItem, BasketItemModel> mapItemToModel;

    private final Basket domain;

    public BasketModeller(final Basket domain) {
        this.domain = domain;
        this.mapItemToModel = new BasketItemToModelMapper();
    }

    public static BasketModeller applyChangesFrom(final Basket domain) {
        return new BasketModeller(domain);
    }

    @Override
    public BasketModel to(final BasketModel from) {
        if (from == null) return null;

        from.setBasketId(domain.id().asText());
        from.setCustomerId(domain.id().asText());

        domain.items()
                .stream()
                .map(mapItemToModel)
                .forEach(from::addItem);

        return null;
    }
}
