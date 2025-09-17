package hu.hirannor.hexagonal.adapter.persistence.jpa.product;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface ProductSpringDataJpaRepository extends Repository<ProductModel, Long> {

    List<ProductModel> findAll();

    Optional<ProductModel> findByProductId(String productId);

    void save(ProductModel model);

    void deleteByProductId(String productId);

}
