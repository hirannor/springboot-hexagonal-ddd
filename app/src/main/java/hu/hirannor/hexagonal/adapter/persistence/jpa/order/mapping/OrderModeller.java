package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyToModelMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.*;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderItemsModel;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderStatus;
import hu.hirannor.hexagonal.domain.order.OrderItem;
import hu.hirannor.hexagonal.infrastructure.modelling.Modeller;

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
        this.mapOrderedProductToModel = new OrderedProductToModelMapper();
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
