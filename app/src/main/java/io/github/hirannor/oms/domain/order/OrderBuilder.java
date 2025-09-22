package io.github.hirannor.oms.domain.order;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;

import java.util.List;

public class OrderBuilder {
    private OrderId id;
    private List<OrderItem> orderItems;
    private OrderStatus status;
    private CustomerId customer;

    public OrderBuilder() {
    }

    public OrderBuilder id(final OrderId id) {
        this.id = id;
        return this;
    }

    public OrderBuilder orderItems(final List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public OrderBuilder status(final OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderBuilder customer(final CustomerId customer) {
        this.customer = customer;
        return this;
    }


    public Order assemble() {
        return new Order(id, orderItems, status, customer);
    }
}