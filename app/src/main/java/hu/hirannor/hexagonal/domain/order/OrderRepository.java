package hu.hirannor.hexagonal.domain.order;

import hu.hirannor.hexagonal.domain.CustomerId;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void deleteBy(OrderId id);
    void save(OrderId order);
    Optional<OrderId> findBy(OrderId id);
    List<OrderId> findBy(CustomerId id);
    List<OrderId> findAll();
}
