package hu.hirannor.hexagonal.application.service.order;

import hu.hirannor.hexagonal.application.usecase.order.OrderCreation;
import hu.hirannor.hexagonal.application.usecase.order.OrderDisplaying;
import hu.hirannor.hexagonal.domain.order.MakeOrder;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
class OrderQueryService implements OrderCreation, OrderDisplaying {

    @Override
    public void create(final MakeOrder command) {

    }

    @Override
    public List<Order> displayAll() {
        return List.of();
    }

    @Override
    public Optional<Order> displayById(final OrderId id) {
        return Optional.empty();
    }
}
