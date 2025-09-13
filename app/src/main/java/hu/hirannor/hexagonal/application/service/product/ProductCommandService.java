package hu.hirannor.hexagonal.application.service.product;

import hu.hirannor.hexagonal.application.usecase.product.ProductCreation;
import hu.hirannor.hexagonal.domain.product.CreateProduct;
import hu.hirannor.hexagonal.domain.product.Product;
import hu.hirannor.hexagonal.domain.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ
)
class ProductCommandService implements ProductCreation {
    private final ProductRepository products;

    @Autowired
    ProductCommandService(final ProductRepository products) {
        this.products = products;
    }

    @Override
    public Product create(final CreateProduct cmd) {
        if (cmd == null) throw new IllegalArgumentException("AddProduct cannot be null");

        final Product toPersist = Product.create(cmd);

        products.save(toPersist);

        return toPersist;
    }
}
