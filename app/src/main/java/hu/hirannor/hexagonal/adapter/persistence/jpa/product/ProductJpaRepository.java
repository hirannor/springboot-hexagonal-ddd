package hu.hirannor.hexagonal.adapter.persistence.jpa.product;

import hu.hirannor.hexagonal.adapter.persistence.jpa.product.mapping.ProductModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.product.mapping.ProductToModelMapper;
import hu.hirannor.hexagonal.domain.product.Product;
import hu.hirannor.hexagonal.domain.product.ProductId;
import hu.hirannor.hexagonal.domain.product.ProductRepository;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
import hu.hirannor.hexagonal.infrastructure.adapter.PersistenceAdapter;
import hu.hirannor.hexagonal.infrastructure.event.EventPublisher;
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
    @EventPublisher
    public Product save(final Product product) {
        if(product == null) throw new IllegalArgumentException("Product cannot be null");

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
}
