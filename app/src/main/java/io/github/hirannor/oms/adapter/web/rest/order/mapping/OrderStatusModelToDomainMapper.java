package io.github.hirannor.oms.adapter.web.rest.order.mapping;

import io.github.hirannor.oms.adapter.web.rest.orders.model.OrderStatusModel;
import io.github.hirannor.oms.domain.order.OrderStatus;

import java.util.function.Function;

public class OrderStatusModelToDomainMapper implements Function<OrderStatusModel, OrderStatus> {
    public OrderStatusModelToDomainMapper() {
    }

    @Override
    public OrderStatus apply(final OrderStatusModel model) {
        if (model == null) return null;

        return switch (model) {
            case WAITING_FOR_PAYMENT -> OrderStatus.WAITING_FOR_PAYMENT;
            case PAID_SUCCESSFULLY -> OrderStatus.PAID_SUCCESSFULLY;
            case PAYMENT_CANCELED -> OrderStatus.PAYMENT_CANCELED;
            case PAYMENT_EXPIRED -> OrderStatus.PAYMENT_EXPIRED;
            case PAYMENT_FAILED -> OrderStatus.PAYMENT_FAILED;
            case PROCESSING -> OrderStatus.PROCESSING;
            case DELIVERED -> OrderStatus.DELIVERED;
            case RETURNED -> OrderStatus.RETURNED;
            case CANCELLED -> OrderStatus.CANCELLED;
            case REFUNDED -> OrderStatus.REFUNDED;
            case SHIPPED -> OrderStatus.SHIPPED;
        };
    }
}
