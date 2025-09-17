package hu.hirannor.hexagonal.application.usecase.order;

import hu.hirannor.hexagonal.domain.order.OrderId;

public interface OrderCancellation {
    void cancelBy(OrderId order);
}
