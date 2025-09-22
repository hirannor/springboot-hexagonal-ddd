package io.github.hirannor.oms.application.usecase.product;

import io.github.hirannor.oms.domain.product.Product;
import io.github.hirannor.oms.domain.product.ProductId;

import java.util.List;
import java.util.Optional;

public interface ProductDisplaying {
    List<Product> displayAll();

    Optional<Product> displayBy(ProductId id);
}
