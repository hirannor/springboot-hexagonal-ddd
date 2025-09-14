package hu.hirannor.hexagonal.adapter.persistence.jpa.product;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(
        propagation = Propagation.MANDATORY,
        isolation = Isolation.REPEATABLE_READ
)
interface ProductSpringDataJpaRepository extends Repository<ProductModel, Long> {

    List<ProductModel> findAll();

    Optional<ProductModel> findByProductId(String productId);

    void save(ProductModel model);

    void deleteByProductId(String productId);

}
