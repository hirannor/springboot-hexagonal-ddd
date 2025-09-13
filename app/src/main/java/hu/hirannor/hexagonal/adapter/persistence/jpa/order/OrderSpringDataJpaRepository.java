package hu.hirannor.hexagonal.adapter.persistence.jpa.order;

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
interface OrderSpringDataJpaRepository extends Repository<OrderModel, Long> {
    OrderModel save(OrderModel order);

    void deleteByOrderId(String order);

    Optional<OrderModel> findByOrderId(String orderId);

    List<OrderModel> findAll();

    List<OrderModel> findByCustomerId(String customerId);

    boolean existsByOrderId(String orderId);
}
