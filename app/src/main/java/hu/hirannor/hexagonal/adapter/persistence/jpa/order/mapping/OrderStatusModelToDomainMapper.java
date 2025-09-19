package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderStatusModel;
import hu.hirannor.hexagonal.domain.order.OrderStatus;
import java.util.function.Function;

public class OrderStatusModelToDomainMapper implements Function<OrderStatusModel, OrderStatus> {

    public OrderStatusModelToDomainMapper() {}

    @Override
    public OrderStatus apply(final OrderStatusModel model) {
        if (model == null) return null;

        return switch (model) {
            case CREATED -> OrderStatus.CREATED;
            case CANCELLED -> OrderStatus.CANCELLED;
            case SHIPPED -> OrderStatus.SHIPPED;
            case REFUNDED -> OrderStatus.REFUNDED;
            case RETURNED -> OrderStatus.RETURNED;
            case DELIVERED -> OrderStatus.DELIVERED;
            case PROCESSING -> OrderStatus.PROCESSING;
            case PAYMENT_FAILED -> OrderStatus.PAYMENT_FAILED;
            case PAYMENT_PENDING -> OrderStatus.PAYMENT_PENDING;
            case PAYMENT_CANCELED -> OrderStatus.PAYMENT_CANCELED;
            case PAID_SUCCESSFULLY -> OrderStatus.PAID_SUCCESSFULLY;
            case WAITING_FOR_PAYMENT -> OrderStatus.WAITING_FOR_PAYMENT;
        };
    }
}
