package io.github.hirannor.oms.adapter.web.rest.product;

import io.github.hirannor.oms.adapter.web.rest.product.mapping.CreateProductModelToCommandMapper;
import io.github.hirannor.oms.adapter.web.rest.product.mapping.ProductToModelMapper;
import io.github.hirannor.oms.adapter.web.rest.products.api.ProductsApi;
import io.github.hirannor.oms.adapter.web.rest.products.model.CreateProductModel;
import io.github.hirannor.oms.adapter.web.rest.products.model.ProductModel;
import io.github.hirannor.oms.application.usecase.product.ProductCreation;
import io.github.hirannor.oms.application.usecase.product.ProductDisplaying;
import io.github.hirannor.oms.domain.product.CreateProduct;
import io.github.hirannor.oms.domain.product.Product;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.adapter.DriverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api")
@DriverAdapter
class ProductController implements ProductsApi {

    private final Function<CreateProductModel, CreateProduct> mapCreateProductModelToCommand;
    private final Function<Product, ProductModel> mapProductToModel;

    private final ProductCreation productCreator;
    private final ProductDisplaying products;

    @Autowired
    ProductController(final ProductCreation productCreator,
                      final ProductDisplaying products) {
        this.mapCreateProductModelToCommand = new CreateProductModelToCommandMapper();
        this.mapProductToModel = new ProductToModelMapper();
        this.productCreator = productCreator;
        this.products = products;
    }

    ProductController(final Function<CreateProductModel, CreateProduct> mapCreateProductModelToCommand,
                      final Function<Product, ProductModel> mapProductToModel,
                      final ProductCreation productCreator,
                      final ProductDisplaying products) {
        this.mapProductToModel = mapProductToModel;
        this.mapCreateProductModelToCommand = mapCreateProductModelToCommand;
        this.productCreator = productCreator;
        this.products = products;
    }

    @Override
    public ResponseEntity<Void> create(final CreateProductModel createProductModel) {
        final CreateProduct create = mapCreateProductModelToCommand.apply(createProductModel);
        final Product product = productCreator.create(create);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.id().value())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<List<ProductModel>> displayAll() {
        final List<ProductModel> products = this.products.displayAll()
                .stream()
                .map(mapProductToModel)
                .toList();

        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<ProductModel> displayBy(final String id) {
        return products.displayBy(ProductId.from(id))
                .map(mapProductToModel
                        .andThen(mapToProductModelResponseEntity()))
                .orElse(ResponseEntity.notFound().build());
    }

    private Function<ProductModel, ResponseEntity<ProductModel>> mapToProductModelResponseEntity() {
        return ResponseEntity::ok;
    }
}
