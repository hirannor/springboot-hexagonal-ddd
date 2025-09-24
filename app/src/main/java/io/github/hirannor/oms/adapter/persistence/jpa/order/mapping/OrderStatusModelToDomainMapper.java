package io.github.hirannor.oms.adapter.persistence.jpa.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.order.OrderStatusModel;
import io.github.hirannor.oms.domain.order.OrderStatus;

import java.util.function.Function;

public class OrderStatusModelToDomainMapper implements Function<OrderStatusModel, OrderStatus> {

    public OrderStatusModelToDomainMapper() {
    }

    @Override
    public OrderStatus apply(final OrderStatusModel model) {
        if (model == null) return null;

        return switch (model) {
            case CANCELLED -> OrderStatus.CANCELLED;
            case SHIPPED -> OrderStatus.SHIPPED;
            case REFUNDED -> OrderStatus.REFUNDED;
            case RETURNED -> OrderStatus.RETURNED;
            case DELIVERED -> OrderStatus.DELIVERED;
            case PROCESSING -> OrderStatus.PROCESSING;
            case PAYMENT_EXPIRED -> OrderStatus.PAYMENT_EXPIRED;
            case PAYMENT_FAILED -> OrderStatus.PAYMENT_FAILED;
            case PAYMENT_CANCELED -> OrderStatus.PAYMENT_CANCELED;
            case PAID_SUCCESSFULLY -> OrderStatus.PAID_SUCCESSFULLY;
            case WAITING_FOR_PAYMENT -> OrderStatus.WAITING_FOR_PAYMENT;
        };
    }
}
