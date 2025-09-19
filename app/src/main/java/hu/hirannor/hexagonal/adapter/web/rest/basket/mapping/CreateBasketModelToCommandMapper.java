package hu.hirannor.hexagonal.adapter.web.rest.basket.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.CreateBasketModel;
import hu.hirannor.hexagonal.domain.basket.command.CreateBasket;
import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import java.util.function.Function;

public class CreateBasketModelToCommandMapper implements Function<CreateBasketModel, CreateBasket> {
    public CreateBasketModelToCommandMapper() {}

    @Override
    public CreateBasket apply(final CreateBasketModel model) {
        if (model == null) return null;

        return CreateBasket.issue(
                CustomerId.from(model.getCustomerId())
        );
    }
}
