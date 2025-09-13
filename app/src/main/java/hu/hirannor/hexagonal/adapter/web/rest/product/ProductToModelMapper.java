package hu.hirannor.hexagonal.adapter.web.rest.product;

import hu.hirannor.hexagonal.adapter.web.rest.products.model.ProductModel;
import hu.hirannor.hexagonal.domain.product.Product;

import java.util.function.Function;

class ProductToModelMapper implements Function<Product, ProductModel> {

    ProductToModelMapper() {}

    @Override
    public ProductModel apply(final Product domain) {
        return null;
    }
}
