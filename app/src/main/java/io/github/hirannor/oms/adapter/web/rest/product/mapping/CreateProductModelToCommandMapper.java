package io.github.hirannor.oms.adapter.web.rest.product.mapping;

import io.github.hirannor.oms.adapter.web.rest.products.model.CreateProductModel;
import io.github.hirannor.oms.adapter.web.rest.products.model.MoneyModel;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.product.CreateProduct;

import java.util.function.Function;

public class CreateProductModelToCommandMapper implements Function<CreateProductModel, CreateProduct> {

    private final Function<MoneyModel, Money> mapModelToDomain;

    public CreateProductModelToCommandMapper() {
        this.mapModelToDomain = new MoneyModelToDomainMapper();
    }

    @Override
    public CreateProduct apply(final CreateProductModel model) {
        if (model == null) return null;

        return CreateProduct.issue(
                model.getName(),
                model.getDescription(),
                mapModelToDomain.apply(model.getPrice())
        );
    }
}
