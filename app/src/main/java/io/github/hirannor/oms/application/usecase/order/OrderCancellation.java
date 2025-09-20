package io.github.hirannor.oms.application.usecase.order;

import io.github.hirannor.oms.domain.order.OrderId;

public interface OrderCancellation {
    void cancelBy(OrderId order);
}
