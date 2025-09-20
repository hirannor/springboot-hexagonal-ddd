package io.github.hirannor.oms.adapter.web.rest.order.mapping;

import io.github.hirannor.oms.adapter.web.rest.orders.model.MoneyModel;
import io.github.hirannor.oms.adapter.web.rest.orders.model.OrderModel;
import io.github.hirannor.oms.adapter.web.rest.orders.model.OrderStatusModel;
import io.github.hirannor.oms.adapter.web.rest.orders.model.OrderedProductModel;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.OrderItem;
import io.github.hirannor.oms.domain.order.OrderStatus;

import java.util.List;
import java.util.function.Function;

public class OrderToModelMapper implements Function<Order, OrderModel> {
    private final Function<OrderStatus, OrderStatusModel> mapStatusToModel;
    private final Function<Money, MoneyModel> mapMoneyToModel;
    private final Function<OrderItem, OrderedProductModel> mapOrderedProductToModel;

    public OrderToModelMapper() {
        this.mapOrderedProductToModel = new OrderedProductToModelMapper();
        this.mapMoneyToModel = new MoneyToModelMapper();
        this.mapStatusToModel = new OrderStatusToModelMapper();
    }

    @Override
    public OrderModel apply(final Order domain) {
        if (domain == null) return null;

        final List<OrderedProductModel> products = domain.orderItems()
                .stream()
                .map(mapOrderedProductToModel)
                .toList();
        return new OrderModel()
                .id(domain.id().asText())
                .orderedProducts(products)
                .customerId(domain.customer().asText())
                .createdAt(domain.createdAt())
                .totalPrice(mapMoneyToModel.apply(domain.totalPrice()))
                .status(mapStatusToModel.apply(domain.status()));
    }
}
