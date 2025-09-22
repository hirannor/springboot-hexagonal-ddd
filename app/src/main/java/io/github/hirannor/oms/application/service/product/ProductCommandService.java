package io.github.hirannor.oms.application.service.product;

import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.application.usecase.product.ProductCreation;
import io.github.hirannor.oms.domain.product.CreateProduct;
import io.github.hirannor.oms.domain.product.Product;
import io.github.hirannor.oms.domain.product.ProductRepository;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationService
class ProductCommandService implements ProductCreation {
    private static final Logger LOGGER = LogManager.getLogger(
            ProductCommandService.class
    );

    private final ProductRepository products;
    private final Outbox outboxes;

    @Autowired
    ProductCommandService(final ProductRepository products, final Outbox outboxes) {
        this.products = products;
        this.outboxes = outboxes;
    }

    @Override
    public Product create(final CreateProduct cmd) {
        if (cmd == null) throw new IllegalArgumentException("AddProduct cannot be null");

        LOGGER.info("Creating new product with productId={}, name={}",
                cmd.productId().asText(),
                cmd.name());

        final Product toPersist = Product.create(cmd);
        products.save(toPersist);

        toPersist.events()
                .forEach(outboxes::save);
        toPersist.clearEvents();

        LOGGER.info("Product with productId={} was created successfully",
                cmd.productId().asText());

        return toPersist;
    }
}
