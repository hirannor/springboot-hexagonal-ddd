package hu.hirannor.hexagonal.adapter.web.rest.basket;

import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.BasketItemModel;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.BasketModel;
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

        final Set<BasketItem> items = model.getItems()
                .stream()
                .map(mapModelToDomain)
                .collect(Collectors.toSet());

        return Basket.from(
                BasketId.from(model.getId()),
                CustomerId.from(model.getCustomerId()),
                items
        );
    }
}
