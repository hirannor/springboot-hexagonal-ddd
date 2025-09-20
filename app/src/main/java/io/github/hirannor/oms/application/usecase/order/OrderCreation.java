package io.github.hirannor.oms.application.usecase.order;

import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.command.CreateOrder;

public interface OrderCreation {
    Order create(CreateOrder command);
}
