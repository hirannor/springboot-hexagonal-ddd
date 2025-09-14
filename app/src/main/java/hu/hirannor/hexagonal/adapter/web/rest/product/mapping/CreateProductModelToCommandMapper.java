package hu.hirannor.hexagonal.adapter.web.rest.product.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.products.model.CreateProductModel;
import hu.hirannor.hexagonal.adapter.web.rest.products.model.MoneyModel;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.product.CreateProduct;

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
