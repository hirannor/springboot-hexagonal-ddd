package io.github.hirannor.oms.adapter.persistence.jpa.order;

import io.github.hirannor.oms.adapter.persistence.jpa.order.mapping.OrderModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.order.mapping.OrderModeller;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.OrderRepository;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import io.github.hirannor.oms.infrastructure.adapter.PersistenceAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@DrivenAdapter
@PersistenceAdapter
class OrderJpaRepository implements OrderRepository {

    private final Function<OrderModel, Order> mapModelToDomain;

    private final OrderSpringDataJpaRepository orders;

    @Autowired
    OrderJpaRepository(final OrderSpringDataJpaRepository orders) {
        this(orders, new OrderModelToDomainMapper());
    }

    OrderJpaRepository(final OrderSpringDataJpaRepository orders,
                       final Function<OrderModel, Order> mapModelToDomain) {
        this.orders = orders;
        this.mapModelToDomain = mapModelToDomain;
    }

    @Override
    public void deleteBy(final OrderId id) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");

        orders.deleteByOrderId(id.asText());

    }

    @Override
    public void save(final Order order) {
        if (order == null) throw new IllegalArgumentException("order cannot be null");

        final OrderModel toPersist = orders.findByOrderId(order.id().asText())
                .orElseGet(OrderModel::new);

        OrderModeller.applyChangesFrom(order).to(toPersist);
        orders.save(toPersist);
    }

    @Override
    public Optional<Order> findBy(final OrderId id) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");

        return orders.findByOrderId(id.asText())
                .map(mapModelToDomain);
    }

    @Override
    public boolean orderExist(final OrderId id) {
        return false;
    }

    @Override
    public List<Order> findBy(final CustomerId id) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");

        return orders.findByCustomerId(id.asText())
                .stream()
                .map(mapModelToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findAll() {
        return orders.findAll()
                .stream()
                .map(mapModelToDomain)
                .collect(Collectors.toList());
    }
}
