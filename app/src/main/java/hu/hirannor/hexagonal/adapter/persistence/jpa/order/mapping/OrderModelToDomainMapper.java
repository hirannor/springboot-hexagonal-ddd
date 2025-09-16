package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderStatusModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderItemsModel;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderStatus;
import hu.hirannor.hexagonal.domain.order.OrderItem;

import java.util.List;
import java.util.function.Function;

public class OrderModelToDomainMapper implements Function<OrderModel, Order> {

    private final Function<OrderStatusModel, OrderStatus> mapStatusModelToDomain;
    private final Function<OrderItemsModel, OrderItem> mapOrderedProductModelToDomain;

    public OrderModelToDomainMapper() {
        this.mapStatusModelToDomain = new OrderStatusModelToDomainMapper();
        this.mapOrderedProductModelToDomain = new OrderedProductModelToDomainMapper();
    }

    @Override
    public Order apply(final OrderModel model) {
        if (model == null) return null;

        final OrderStatus status = mapStatusModelToDomain.apply(model.getStatus());
        final List<OrderItem> orderItems = model.getOrderItems()
                .stream()
                .map(mapOrderedProductModelToDomain)
                .toList();

        return Order
                .empty()
                .id(OrderId.from(model.getOrderId()))
                .customer(CustomerId.from(model.getCustomerId()))
                .status(status)
                .orderItems(orderItems)
                .assemble();

    }
}
