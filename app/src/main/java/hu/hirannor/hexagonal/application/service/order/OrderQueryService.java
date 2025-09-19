package hu.hirannor.hexagonal.application.service.order;

import hu.hirannor.hexagonal.application.usecase.order.OrderDisplaying;
import hu.hirannor.hexagonal.domain.order.*;
import hu.hirannor.hexagonal.infrastructure.application.ApplicationService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

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
