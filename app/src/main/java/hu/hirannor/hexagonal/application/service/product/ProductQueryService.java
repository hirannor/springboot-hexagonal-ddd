package hu.hirannor.hexagonal.application.service.product;

import hu.hirannor.hexagonal.application.usecase.product.ProductDisplaying;
import hu.hirannor.hexagonal.domain.product.Product;
import hu.hirannor.hexagonal.domain.product.ProductId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
class ProductQueryService implements ProductDisplaying {

    @Override
    public List<Product> displayAll() {
        return List.of();
    }

    @Override
    public Optional<Product> displayBy(ProductId id) {
        return Optional.empty();
    }
}
