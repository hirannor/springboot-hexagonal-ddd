package io.github.hirannor.oms.domain.order;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void deleteBy(OrderId id);

    void save(Order order);

    Optional<Order> findBy(OrderId id);

    boolean orderExist(OrderId id);

    List<Order> findBy(CustomerId id);

    List<Order> findAll();
}
