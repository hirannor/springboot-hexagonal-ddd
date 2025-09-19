package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderStatusModel;
import hu.hirannor.hexagonal.domain.order.OrderStatus;
import java.util.function.Function;

public class OrderStatusToModelMapper implements Function<OrderStatus, OrderStatusModel> {

    public OrderStatusToModelMapper() {}

    @Override
    public OrderStatusModel apply(final OrderStatus domain) {
        if (domain == null) return null;

        return switch (domain) {
            case CREATED -> OrderStatusModel.CREATED;
            case CANCELLED -> OrderStatusModel.CANCELLED;
            case SHIPPED -> OrderStatusModel.SHIPPED;
            case REFUNDED -> OrderStatusModel.REFUNDED;
            case RETURNED -> OrderStatusModel.RETURNED;
            case DELIVERED -> OrderStatusModel.DELIVERED;
            case PROCESSING -> OrderStatusModel.PROCESSING;
            case PAYMENT_FAILED -> OrderStatusModel.PAYMENT_FAILED;
            case PAYMENT_CANCELED -> OrderStatusModel.PAYMENT_CANCELED;
            case PAID_SUCCESSFULLY -> OrderStatusModel.PAID_SUCCESSFULLY;
            case WAITING_FOR_PAYMENT -> OrderStatusModel.WAITING_FOR_PAYMENT;
        };
    }
}
