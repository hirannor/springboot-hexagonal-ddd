package hu.hirannor.hexagonal.adapter.web.rest.product;

import hu.hirannor.hexagonal.adapter.web.rest.products.model.CreateProductModel;
import hu.hirannor.hexagonal.domain.product.CreateProduct;

import java.util.function.Function;

class CreateProductModelToCommandMapper implements Function<CreateProductModel, CreateProduct> {

    CreateProductModelToCommandMapper() {}

    @Override
    public CreateProduct apply(final CreateProductModel model) {
        return null;
    }
}
