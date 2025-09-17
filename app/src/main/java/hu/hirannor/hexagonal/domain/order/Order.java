package hu.hirannor.hexagonal.domain.order;

import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.domain.core.valueobject.Money;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;
import hu.hirannor.hexagonal.domain.order.events.*;
import hu.hirannor.hexagonal.domain.payment.PaymentReceipt;
import hu.hirannor.hexagonal.infrastructure.aggregate.AggregateRoot;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class Order extends AggregateRoot {

    private OrderStatus status;

    private final OrderId id;
    private final List<OrderItem> orderItems;
    private final CustomerId customer;
    private final Instant createdAt;
    private final List<OrderStatusChange> history;
    private final List<DomainEvent> events;

    Order(final OrderId id,
          final List<OrderItem> orderItems,
          final OrderStatus status,
          final CustomerId customer) {

        this.id = Objects.requireNonNull(id, "OrderId cannot be null");
        this.orderItems = Objects.requireNonNull(orderItems, "Order items cannot be null");
        this.status = Objects.requireNonNull(status, "OrderStatus cannot be null");
        this.customer = Objects.requireNonNull(customer, "CustomerId cannot be null");

        this.createdAt = Instant.now();
        this.history = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public static OrderBuilder empty() {
        return new OrderBuilder();
    }

    public static Order create(final CreateOrder command) {
        Objects.requireNonNull(command, "CreateOrder cannot be null");

        if (command.orderItems().isEmpty()) throw new IllegalArgumentException("Order must have at least one product");

        final Order createdOrder = empty()
                .id(command.orderId())
                .orderItems(command.orderItems())
                .status(OrderStatus.CREATED)
                .customer(command.customer())
                .assemble();

        createdOrder.events.add(OrderCreated.record(createdOrder.id, command.customer()));

        createdOrder.markAsWaitingForPayment();

        return createdOrder;
    }

    public OrderId id() {
        return id;
    }

    public OrderStatus status() {
        return status;
    }

    public List<OrderItem> orderItems() {
        return Collections.unmodifiableList(orderItems);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public CustomerId customer() {
        return customer;
    }

    public void changeStatus(final OrderStatus target) {
        if (!status.canTransitionTo(target))
            throw new IllegalStateException("Cannot change to " + target +  " status");

        this.history.add(OrderStatusChange.from(status, target, Instant.now()));

        switch (target) {
            case SHIPPED -> ship();
            case DELIVERED -> deliver();
            case CANCELLED -> cancel();
            case RETURNED -> returnOrder();
            case REFUNDED -> refund();
        }
    }

    public void handlePaymentResult(final PaymentReceipt receipt) {
        Objects.requireNonNull(receipt, "Receipt cannot be null");

        switch (receipt.status()) {
            case SUCCEEDED -> markAsPaidSuccessfully();
            case FAILED -> markAsPaymentFailed();
            case CANCELED -> markAsPaymentCanceled();
            case INITIALIZED -> {} // NOOP
        }
    }

    public void ship() {
        if (!status.canTransitionTo(OrderStatus.SHIPPED))
            throw new IllegalStateException("Cannot ship order from status " + status);

        addHistory(status, OrderStatus.SHIPPED);

        this.status = OrderStatus.SHIPPED;
        events.add(OrderShipped.record(id, customer));
    }

    public void deliver() {
        if (!status.canTransitionTo(OrderStatus.DELIVERED))
            throw new IllegalStateException("Cannot deliver order from status " + status);

        addHistory(status, OrderStatus.DELIVERED);

        this.status = OrderStatus.DELIVERED;
        events.add(OrderDelivered.record(id, customer));
    }

    public void cancel() {
        if (!status.canTransitionTo(OrderStatus.CANCELLED))
            throw new IllegalStateException("Cannot cancel order from status " + status);

        addHistory(status, OrderStatus.CANCELLED);

        this.status = OrderStatus.CANCELLED;
        events.add(OrderCanceled.record(id, customer));
    }

    public void returnOrder() {
        if (!status.canTransitionTo(OrderStatus.RETURNED))
            throw new IllegalStateException("Cannot return order from status " + status);

        addHistory(status, OrderStatus.RETURNED);

        this.status = OrderStatus.RETURNED;
        events.add(OrderReturned.record(id, customer));
    }

    public void refund() {
        if (!status.canTransitionTo(OrderStatus.REFUNDED))
            throw new IllegalStateException("Cannot refund order from status " + status);

        addHistory(status, OrderStatus.REFUNDED);

        this.status = OrderStatus.REFUNDED;
        events.add(OrderRefunded.record(id, customer));
    }

    public Money totalPrice() {
        return orderItems.stream()
                .map(OrderItem::lineTotal)
                .reduce(Money::add)
                .orElseThrow(failBecauseOrderDoesntContainProduct());
    }

    public List<OrderStatusChange> history() { return Collections.unmodifiableList(history); }

    @Override
    public void clearEvents() {
        events.clear();
    }

    @Override
    public List<DomainEvent> events() {
        return Collections.unmodifiableList(events);
    }

    private void startProcessing() {
        if (!status.canTransitionTo(OrderStatus.PROCESSING))
            throw new IllegalStateException("Cannot start processing from status " + status);

        addHistory(status, OrderStatus.PROCESSING);

        this.status = OrderStatus.PROCESSING;
        events.add(OrderProcessing.record(id, customer));
    }

    private void markAsWaitingForPayment() {
        if (!status.canTransitionTo(OrderStatus.WAITING_FOR_PAYMENT))
            throw new IllegalStateException("Cannot mark as WAITING_FOR_PAYMENT from " + status);

        addHistory(status, OrderStatus.WAITING_FOR_PAYMENT);
        this.status = OrderStatus.WAITING_FOR_PAYMENT;
    }

    private void markAsPaidSuccessfully() {
        if (!status.canTransitionTo(OrderStatus.PAID_SUCCESSFULLY))
            throw new IllegalStateException("Cannot mark as PAID_SUCCESSFULLY from " + status);

        addHistory(status, OrderStatus.PAID_SUCCESSFULLY);
        this.status = OrderStatus.PAID_SUCCESSFULLY;
        events.add(OrderPaid.record(id, customer));

        startProcessing();
    }

    private void markAsPaymentFailed() {
        if (!status.canTransitionTo(OrderStatus.PAYMENT_FAILED))
            throw new IllegalStateException("Cannot mark as PAYMENT_FAILED from " + status);

        addHistory(status, OrderStatus.PAYMENT_FAILED);
        this.status = OrderStatus.PAYMENT_FAILED;
        events.add(OrderPaymentFailed.record(id, customer));
    }

    private void markAsPaymentCanceled() {
        if (!status.canTransitionTo(OrderStatus.PAYMENT_CANCELED))
            throw new IllegalStateException("Cannot mark as PAYMENT_CANCELED from " + status);

        addHistory(status, OrderStatus.PAYMENT_CANCELED);
        this.status = OrderStatus.PAYMENT_CANCELED;
        events.add(OrderPaymentCanceled.record(id, customer));
    }

    private void addHistory(final OrderStatus from, final OrderStatus to) {
        this.history.add(OrderStatusChange.from(from, to, Instant.now()));
    }

    private Supplier<IllegalStateException> failBecauseOrderDoesntContainProduct() {
        return () -> new IllegalStateException("Order must contain at least one product");
    }

}
