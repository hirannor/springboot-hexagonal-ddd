package io.github.hirannor.oms.adapter.persistence.jpa.basket;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface BasketSpringDataJpaRepository extends Repository<BasketModel, Long> {

    BasketModel save(BasketModel basket);

    Optional<BasketModel> findByBasketId(String basketId);

    Optional<BasketModel> findByCustomerId(String customerId);

    List<BasketModel> findAll();

    void deleteByCustomerId(String customerId);
}
