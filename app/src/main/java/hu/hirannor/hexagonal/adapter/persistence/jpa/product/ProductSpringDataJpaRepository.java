package hu.hirannor.hexagonal.adapter.persistence.jpa.product;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

interface ProductSpringDataJpaRepository extends Repository<ProductModel, Long> {

    List<ProductModel> findAll();

    List<ProductModel> findAllByProductIdIn(List<String> productIds);

    Optional<ProductModel> findByProductId(String productId);

    void save(ProductModel model);

    void deleteByProductId(String productId);

}
