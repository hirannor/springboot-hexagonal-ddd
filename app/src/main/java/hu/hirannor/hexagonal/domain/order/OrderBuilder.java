package hu.hirannor.hexagonal.domain.order;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.payment.PaymentTransaction;

import java.util.List;

public class OrderBuilder {
    private OrderId id;
    private List<OrderItem> orderItems;
    private OrderStatus status;
    private CustomerId customer;
    private PaymentTransaction transaction;

    public OrderBuilder() {}

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


    public OrderBuilder transaction(final PaymentTransaction transaction) {
        this.transaction = transaction;
        return this;
    }

    public Order assemble() {
        return new Order(id, orderItems, status, customer, transaction);
    }
}