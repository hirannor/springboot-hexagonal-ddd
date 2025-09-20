package io.github.hirannor.oms.application.service.order;

import io.github.hirannor.oms.application.usecase.order.OrderDisplaying;
import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.OrderRepository;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@ApplicationService
class OrderQueryService implements OrderDisplaying {

    private final OrderRepository orders;

    @Autowired
    OrderQueryService(final OrderRepository orders) {
        this.orders = orders;
    }

    @Override
    public List<Order> displayAll() {
        return orders.findAll();
    }

    @Override
    public Optional<Order> displayBy(final OrderId id) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");

        return orders.findBy(id);
    }
}
