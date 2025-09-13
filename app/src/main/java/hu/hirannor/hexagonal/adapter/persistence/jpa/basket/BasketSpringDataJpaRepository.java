package hu.hirannor.hexagonal.adapter.persistence.jpa.basket;

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
interface BasketSpringDataJpaRepository extends Repository<BasketModel, Long> {

    BasketModel save(BasketModel basket);

    Optional<BasketModel> findByBasketId(String basketId);

    Optional<BasketModel> findByCustomerId(String customerId);

    List<BasketModel> findAll();

    void deleteByCustomerId(String customerId);
}
