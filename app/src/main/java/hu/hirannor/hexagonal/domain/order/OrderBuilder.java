package hu.hirannor.hexagonal.domain.order;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.payment.PaymentTransaction;

import java.util.Set;

public class OrderBuilder {
    private OrderId id;
    private Set<OrderedProduct> orderedProducts;
    private OrderStatus status;
    private CustomerId customer;
    private PaymentTransaction transaction;

    public OrderBuilder() {}

    public OrderBuilder id(final OrderId id) {
        this.id = id;
        return this;
    }

    public OrderBuilder orderedProducts(final Set<OrderedProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
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
        return new Order(id, orderedProducts, status, customer, transaction);
    }
}