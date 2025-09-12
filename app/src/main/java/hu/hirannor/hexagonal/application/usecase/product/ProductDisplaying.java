package hu.hirannor.hexagonal.application.usecase.product;

import hu.hirannor.hexagonal.domain.product.Product;
import hu.hirannor.hexagonal.domain.product.ProductId;

import java.util.List;
import java.util.Optional;

public interface ProductDisplaying {
    List<Product> displayAll();
    Optional<Product> displayBy(ProductId id);
}
