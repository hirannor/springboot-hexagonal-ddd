package io.github.hirannor.oms.adapter.persistence.jpa.order;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface OrderSpringDataJpaRepository extends Repository<OrderModel, Long> {
    OrderModel save(OrderModel order);

    void deleteByOrderId(String order);

    Optional<OrderModel> findByOrderId(String orderId);

    List<OrderModel> findAll();

    List<OrderModel> findByCustomerId(String customerId);

    boolean existsByOrderId(String orderId);
}
