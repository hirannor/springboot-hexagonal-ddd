package hu.hirannor.hexagonal.adapter.persistence.jpa.order;

import hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping.OrderModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping.OrderModeller;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderRepository;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
import hu.hirannor.hexagonal.infrastructure.event.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Transactional(
    propagation = Propagation.MANDATORY,
    isolation = Isolation.REPEATABLE_READ
)
@DrivenAdapter
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
    @EventPublisher
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
