package hu.hirannor.hexagonal.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void deleteById(ProductId id);
    void save(ProductId order);
    Optional<Product> findById(ProductId id);
    List<Product> findAll();
}
