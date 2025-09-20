package io.github.hirannor.oms.application.usecase.product;

import io.github.hirannor.oms.domain.product.CreateProduct;
import io.github.hirannor.oms.domain.product.Product;

public interface ProductCreation {
    Product create(CreateProduct cmd);
}
