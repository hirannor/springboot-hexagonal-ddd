package hu.hirannor.hexagonal.adapter.web.rest.order;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.OrderModel;
import hu.hirannor.hexagonal.domain.order.Order;

import java.util.function.Function;

class OrderToModelMapper implements Function<Order, OrderModel> {
    OrderToModelMapper() {}

    @Override
    public OrderModel apply(Order domain) {
        return null;
    }
}
