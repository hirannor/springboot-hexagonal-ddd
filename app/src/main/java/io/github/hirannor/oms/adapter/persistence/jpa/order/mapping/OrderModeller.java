package io.github.hirannor.oms.adapter.persistence.jpa.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.order.OrderItemsModel;
import io.github.hirannor.oms.adapter.persistence.jpa.order.OrderModel;
import io.github.hirannor.oms.adapter.persistence.jpa.order.OrderStatusModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.OrderItem;
import io.github.hirannor.oms.domain.order.OrderStatus;
import io.github.hirannor.oms.infrastructure.modelling.Modeller;

import java.util.List;
import java.util.function.Function;

public class OrderModeller implements Modeller<OrderModel> {

    private final Function<Currency, CurrencyModel> mapCurrencyToModel;
    private final Function<OrderStatus, OrderStatusModel> mapStatusToModel;
    private final Function<OrderItem, OrderItemsModel> mapOrderedProductToModel;

    private final Order domain;

    public OrderModeller(final Order domain) {
        this.domain = domain;
        this.mapCurrencyToModel = new CurrencyToModelMapper();
        this.mapStatusToModel = new OrderStatusToModelMapper();
        this.mapOrderedProductToModel = new OrderedItemToModelMapper();
    }

    public static OrderModeller applyChangesFrom(final Order domain) {
        return new OrderModeller(domain);
    }

    @Override
    public OrderModel to(final OrderModel from) {
        if (from == null) return null;

        from.setOrderId(domain.id().asText());
        from.setCustomerId(domain.customer().asText());
        from.setCreatedAt(domain.createdAt());
        from.setTotalPriceAmount(domain.totalPrice().amount());
        from.setTotalPriceCurrency(mapCurrencyToModel.apply(domain.totalPrice().currency()));
        from.setStatus(mapStatusToModel.apply(domain.status()));

        final List<OrderItemsModel> orderItems = domain.orderItems()
                .stream()
                .map(mapOrderedProductToModel)
                .toList();

        from.setOrderItems(orderItems);

        return from;
    }
}
