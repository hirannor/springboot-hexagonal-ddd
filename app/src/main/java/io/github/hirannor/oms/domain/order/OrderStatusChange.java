package io.github.hirannor.oms.domain.order;

import java.time.Instant;

public record OrderStatusChange(OrderStatus from, OrderStatus to, Instant at) {
    public static OrderStatusChange from(final OrderStatus from, final OrderStatus to, final Instant at) {
        return new OrderStatusChange(from, to, at);
    }
}
