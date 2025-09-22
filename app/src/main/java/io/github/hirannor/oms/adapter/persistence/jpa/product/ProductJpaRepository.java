package io.github.hirannor.oms.adapter.persistence.jpa.product;

import io.github.hirannor.oms.adapter.persistence.jpa.product.mapping.ProductModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.product.mapping.ProductToModelMapper;
import io.github.hirannor.oms.domain.product.Product;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.domain.product.ProductRepository;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import io.github.hirannor.oms.infrastructure.adapter.PersistenceAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@DrivenAdapter
@PersistenceAdapter
class ProductJpaRepository implements ProductRepository {
    private final Function<ProductModel, Product> mapModelToDomain;
    private final Function<Product, ProductModel> mapDomainToModel;

    private final ProductSpringDataJpaRepository products;

    @Autowired
    ProductJpaRepository(final ProductSpringDataJpaRepository products) {
        this(products, new ProductModelToDomainMapper(), new ProductToModelMapper());
    }

    ProductJpaRepository(final ProductSpringDataJpaRepository products,
                         final Function<ProductModel, Product> mapModelToDomain,
                         final Function<Product, ProductModel> mapDomainToModel) {
        this.products = products;
        this.mapModelToDomain = mapModelToDomain;
        this.mapDomainToModel = mapDomainToModel;
    }

    @Override
    public void deleteById(final ProductId id) {
        if (id == null) throw new IllegalArgumentException("Product id cannot be null");

        products.deleteByProductId(id.asText());
    }

    @Override
    public Product save(final Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");

        products.save(mapDomainToModel.apply(product));
        return product;
    }

    @Override
    public Optional<Product> findById(final ProductId id) {
        if (id == null) throw new IllegalArgumentException("Product id cannot be null");

        return products.findByProductId(id.asText())
                .map(mapModelToDomain);
    }

    @Override
    public List<Product> findAll() {
        return products.findAll()
                .stream()
                .map(mapModelToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findAllBy(final List<ProductId> productIds) {
        final List<String> rawIds = productIds.stream()
                .map(ProductId::value)
                .toList();

        return products.findAllByProductIdIn(rawIds)
                .stream()
                .map(mapModelToDomain)
                .toList();
    }
}
