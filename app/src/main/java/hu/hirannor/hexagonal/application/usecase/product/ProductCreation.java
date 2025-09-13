package hu.hirannor.hexagonal.application.usecase.product;

import hu.hirannor.hexagonal.domain.product.CreateProduct;
import hu.hirannor.hexagonal.domain.product.Product;

public interface ProductCreation {
    Product create(CreateProduct cmd);
}
