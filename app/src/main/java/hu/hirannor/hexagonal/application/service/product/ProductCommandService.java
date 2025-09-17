package hu.hirannor.hexagonal.application.service.product;

import hu.hirannor.hexagonal.application.usecase.product.ProductCreation;
import hu.hirannor.hexagonal.domain.product.CreateProduct;
import hu.hirannor.hexagonal.domain.product.Product;
import hu.hirannor.hexagonal.domain.product.ProductRepository;
import hu.hirannor.hexagonal.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationService
class ProductCommandService implements ProductCreation {
    private static final Logger LOGGER = LogManager.getLogger(
        ProductCommandService.class
    );

    private final ProductRepository products;

    @Autowired
    ProductCommandService(final ProductRepository products) {
        this.products = products;
    }

    @Override
    public Product create(final CreateProduct cmd) {
        if (cmd == null) throw new IllegalArgumentException("AddProduct cannot be null");

        LOGGER.info("Creating new product with id: {}, name: {}",
            cmd.productId().asText(),
            cmd.name());

        final Product toPersist = Product.create(cmd);
        products.save(toPersist);

        LOGGER.info("Product with id: {} was created successfully",
            cmd.productId().asText());

        return toPersist;
    }
}
