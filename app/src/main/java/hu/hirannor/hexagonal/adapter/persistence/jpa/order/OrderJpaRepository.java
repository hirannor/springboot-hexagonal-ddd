package hu.hirannor.hexagonal.adapter.persistence.jpa.order;

import hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping.OrderModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping.OrderToModelMapper;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderRepository;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
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

    private final Function<Order, OrderModel> mapDomainToModel;
    private final Function<OrderModel, Order> mapModelToDomain;

    private final OrderSpringDataJpaRepository orders;

    @Autowired
    OrderJpaRepository(final OrderSpringDataJpaRepository orders) {
        this(orders, new OrderToModelMapper(), new OrderModelToDomainMapper());
    }

    OrderJpaRepository(final OrderSpringDataJpaRepository orders,
                       final Function<Order, OrderModel> mapDomainToModel,
                       final Function<OrderModel, Order> mapModelToDomain) {
        this.orders = orders;
        this.mapDomainToModel = mapDomainToModel;
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

        orders.save(
            mapDomainToModel.apply(order)
        );

    }

    @Override
    public Optional<Order> findBy(final OrderId id) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");

        return orders.findByOrderId(id.asText())
                .map(mapModelToDomain);
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
