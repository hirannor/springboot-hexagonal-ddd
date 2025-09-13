package hu.hirannor.hexagonal.application.service.product;

import hu.hirannor.hexagonal.application.usecase.product.ProductDisplaying;
import hu.hirannor.hexagonal.domain.product.Product;
import hu.hirannor.hexagonal.domain.product.ProductId;
import hu.hirannor.hexagonal.domain.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
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
