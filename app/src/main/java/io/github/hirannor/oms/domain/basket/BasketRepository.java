package io.github.hirannor.oms.domain.basket;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;

import java.util.List;
import java.util.Optional;

public interface BasketRepository {

    Optional<Basket> findBy(CustomerId customerId);

    Optional<Basket> findBy(BasketId basketId);

    List<Basket> findAll();

    void save(Basket basket);

    void deleteBy(CustomerId basket);
}
