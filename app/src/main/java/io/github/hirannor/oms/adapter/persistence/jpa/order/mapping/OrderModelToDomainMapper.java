package io.github.hirannor.oms.adapter.persistence.jpa.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.order.OrderItemsModel;
import io.github.hirannor.oms.adapter.persistence.jpa.order.OrderModel;
import io.github.hirannor.oms.adapter.persistence.jpa.order.OrderStatusModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.OrderItem;
import io.github.hirannor.oms.domain.order.OrderStatus;

import java.util.List;
import java.util.function.Function;

public class OrderModelToDomainMapper implements Function<OrderModel, Order> {

    private final Function<OrderStatusModel, OrderStatus> mapStatusModelToDomain;
    private final Function<OrderItemsModel, OrderItem> mapOrderedProductModelToDomain;

    public OrderModelToDomainMapper() {
        this.mapStatusModelToDomain = new OrderStatusModelToDomainMapper();
        this.mapOrderedProductModelToDomain = new OrderedItemModelToDomainMapper();
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
