package io.github.hirannor.oms.adapter.web.rest.order.mapping;

import io.github.hirannor.oms.adapter.web.rest.orders.model.OrderStatusModel;
import io.github.hirannor.oms.domain.order.OrderStatus;

import java.util.function.Function;

public class OrderStatusToModelMapper implements Function<OrderStatus, OrderStatusModel> {
    public OrderStatusToModelMapper() {
    }

    @Override
    public OrderStatusModel apply(final OrderStatus domain) {
        if (domain == null) return null;

        return switch (domain) {
            case WAITING_FOR_PAYMENT -> OrderStatusModel.WAITING_FOR_PAYMENT;
            case PAID_SUCCESSFULLY -> OrderStatusModel.PAID_SUCCESSFULLY;
            case PAYMENT_CANCELED -> OrderStatusModel.PAYMENT_CANCELED;
            case PAYMENT_FAILED -> OrderStatusModel.PAYMENT_FAILED;
            case PROCESSING -> OrderStatusModel.PROCESSING;
            case PAYMENT_EXPIRED -> OrderStatusModel.PAYMENT_EXPIRED;
            case DELIVERED -> OrderStatusModel.DELIVERED;
            case RETURNED -> OrderStatusModel.RETURNED;
            case CANCELLED -> OrderStatusModel.CANCELLED;
            case REFUNDED -> OrderStatusModel.REFUNDED;
            case SHIPPED -> OrderStatusModel.SHIPPED;
        };
    }
}
