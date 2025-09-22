package io.github.hirannor.oms.application.usecase.order;

import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.OrderId;

import java.util.List;
import java.util.Optional;

public interface OrderDisplaying {
    List<Order> displayAll();

    Optional<Order> displayBy(OrderId id);
}
