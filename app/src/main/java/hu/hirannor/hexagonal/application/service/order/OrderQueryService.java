package hu.hirannor.hexagonal.application.service.order;

import hu.hirannor.hexagonal.application.usecase.order.OrderDisplaying;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
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
