package hu.hirannor.hexagonal.application.service.product;

import hu.hirannor.hexagonal.application.usecase.product.ProductDisplaying;
import hu.hirannor.hexagonal.domain.product.*;
import hu.hirannor.hexagonal.infrastructure.application.ApplicationService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationService
class ProductQueryService implements ProductDisplaying {
    private final ProductRepository products;

    @Autowired
    ProductQueryService(final ProductRepository products) {
        this.products = products;
    }

    @Override
    public List<Product> displayAll() {
        return products.findAll();
    }

    @Override
    public Optional<Product> displayBy(final ProductId id) {
        return products.findById(id);
    }
}
