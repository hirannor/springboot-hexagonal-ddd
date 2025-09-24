package io.github.hirannor.oms.adapter.persistence.jpa.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.order.OrderStatusModel;
import io.github.hirannor.oms.domain.order.OrderStatus;

import java.util.function.Function;

public class OrderStatusToModelMapper implements Function<OrderStatus, OrderStatusModel> {

    public OrderStatusToModelMapper() {
    }

    @Override
    public OrderStatusModel apply(final OrderStatus domain) {
        if (domain == null) return null;

        return switch (domain) {
            case CANCELLED -> OrderStatusModel.CANCELLED;
            case SHIPPED -> OrderStatusModel.SHIPPED;
            case REFUNDED -> OrderStatusModel.REFUNDED;
            case RETURNED -> OrderStatusModel.RETURNED;
            case DELIVERED -> OrderStatusModel.DELIVERED;
            case PROCESSING -> OrderStatusModel.PROCESSING;
            case PAYMENT_FAILED -> OrderStatusModel.PAYMENT_FAILED;
            case PAYMENT_EXPIRED -> OrderStatusModel.PAYMENT_EXPIRED;
            case PAYMENT_CANCELED -> OrderStatusModel.PAYMENT_CANCELED;
            case PAID_SUCCESSFULLY -> OrderStatusModel.PAID_SUCCESSFULLY;
            case WAITING_FOR_PAYMENT -> OrderStatusModel.WAITING_FOR_PAYMENT;
        };
    }
}
