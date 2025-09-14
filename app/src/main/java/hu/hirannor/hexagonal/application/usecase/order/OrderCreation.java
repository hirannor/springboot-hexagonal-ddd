package hu.hirannor.hexagonal.application.usecase.order;

import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;

public interface OrderCreation {
    Order create(CreateOrder command);
}
