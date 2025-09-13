package hu.hirannor.hexagonal.application.usecase.order;

import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.command.MakeOrder;

public interface OrderCreation {
    Order create(MakeOrder command);
}
