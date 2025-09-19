package hu.hirannor.hexagonal.adapter.persistence.jpa.basket;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

interface BasketSpringDataJpaRepository extends Repository<BasketModel, Long> {

    BasketModel save(BasketModel basket);

    Optional<BasketModel> findByBasketId(String basketId);

    Optional<BasketModel> findByCustomerId(String customerId);

    List<BasketModel> findAll();

    void deleteByCustomerId(String customerId);
}
