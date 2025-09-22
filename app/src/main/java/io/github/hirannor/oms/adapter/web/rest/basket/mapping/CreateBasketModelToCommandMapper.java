package io.github.hirannor.oms.adapter.web.rest.basket.mapping;

import io.github.hirannor.oms.adapter.web.rest.baskets.model.CreateBasketModel;
import io.github.hirannor.oms.domain.basket.command.CreateBasket;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;

import java.util.function.Function;

public class CreateBasketModelToCommandMapper implements Function<CreateBasketModel, CreateBasket> {
    public CreateBasketModelToCommandMapper() {
    }

    @Override
    public CreateBasket apply(final CreateBasketModel model) {
        if (model == null) return null;

        return CreateBasket.issue(
                CustomerId.from(model.getCustomerId())
        );
    }
}
