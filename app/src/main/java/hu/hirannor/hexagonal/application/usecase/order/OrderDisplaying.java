package hu.hirannor.hexagonal.application.usecase.order;

import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import java.util.List;
import java.util.Optional;

public interface OrderDisplaying {
    List<Order> displayAll();
    Optional<Order> displayBy(OrderId id);
}
