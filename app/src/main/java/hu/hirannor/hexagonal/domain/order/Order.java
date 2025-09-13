package hu.hirannor.hexagonal.domain.order;

import hu.hirannor.hexagonal.application.service.payment.PaymentReceipt;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.command.MakeOrder;
import hu.hirannor.hexagonal.domain.order.events.*;
import hu.hirannor.hexagonal.infrastructure.aggregate.AggregateRoot;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class Order extends AggregateRoot {

    private final OrderId id;
    private OrderStatus status;
    private final Set<OrderedProduct> orderedProducts;
    private final CustomerId customer;
    private final Instant createdAt;
    private final List<DomainEvent> events;

    Order(final OrderId id,
          final Set<OrderedProduct> orderedProducts,
          final OrderStatus status,
          final CustomerId customer) {
        Objects.requireNonNull(id, "OrderId cannot be null");
        Objects.requireNonNull(orderedProducts, "Ordered products cannot be null");
        Objects.requireNonNull(status, "OrderStatus cannot be null");

        this.id = id;
        this.orderedProducts = orderedProducts;
        this.status = status;
        this.createdAt = Instant.now();
        this.customer = customer;
        this.events = new ArrayList<>();
    }

    public static OrderBuilder empty() {
        return new OrderBuilder();
    }

    public static Order create(final MakeOrder command) {
        Objects.requireNonNull(command, "MakeOrder command cannot be null");

        if (command.products().isEmpty()) throw new IllegalArgumentException("Order must have at least one product");

        final Order createdOrder = empty()
                .id(OrderId.generate())
                .orderedProducts(command.products())
                .status(OrderStatus.CREATED)
                .customer(command.customer())
                .assemble();

        createdOrder.events.add(OrderCreated.create(createdOrder.id, command.customer()));

        return createdOrder;
    }

    public OrderId id() {
        return id;
    }

    public OrderStatus status() {
        return status;
    }

    public Set<OrderedProduct> products() {
        return Collections.unmodifiableSet(orderedProducts);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public CustomerId customer() {
        return customer;
    }

    public void handlePaymentResult(final PaymentReceipt receipt) {
        switch (receipt.status()) {
            case SUCCESS -> markAsPaid(customer);
            case PENDING -> markPaymentPending();
            case CANCELLED -> markPaymentCanceled();
            case FAILURE -> markPaymentFailed();
            default -> throw new IllegalStateException("Unknown payment status: " + receipt.status());
        }
    }

    public void changeStatus(final OrderStatus target) {
        if (!status.canTransitionTo(target)) {
            throw new IllegalStateException("Cannot change to " + target +  " status");
        }

        this.status = target;
    }

    public void startProcessing() {
        if (!status.canTransitionTo(OrderStatus.PROCESSING)) {
            throw new IllegalStateException("Cannot start processing from status " + status);
        }
        this.status = OrderStatus.PROCESSING;
        events.add(OrderProcessing.record(id, customer));

    }

    public void ship() {
        if (!status.canTransitionTo(OrderStatus.SHIPPED)) {
            throw new IllegalStateException("Cannot ship order from status " + status);
        }
        this.status = OrderStatus.SHIPPED;
        events.add(OrderShipped.record(id, customer));
    }

    public void deliver() {
        if (!status.canTransitionTo(OrderStatus.DELIVERED)) {
            throw new IllegalStateException("Cannot deliver order from status " + status);
        }
        this.status = OrderStatus.DELIVERED;
        events.add(OrderDelivered.record(id, customer));
    }

    public void cancel() {
        if (!status.canTransitionTo(OrderStatus.CANCELLED)) {
            throw new IllegalStateException("Cannot cancel order from status " + status);
        }
        this.status = OrderStatus.CANCELLED;
        events.add(OrderCanceled.record(id, customer));
    }

    public void returnOrder() {
        if (!status.canTransitionTo(OrderStatus.RETURNED)) {
            throw new IllegalStateException("Cannot return order from status " + status);
        }
        this.status = OrderStatus.RETURNED;
        events.add(OrderReturned.record(id, customer));
    }

    public void refund() {
        if (!status.canTransitionTo(OrderStatus.REFUNDED)) {
            throw new IllegalStateException("Cannot refund order from status " + status);
        }
        this.status = OrderStatus.REFUNDED;
        events.add(OrderRefunded.record(id, customer));
    }

    public Money totalPrice() {
        return orderedProducts.stream()
                .map(OrderedProduct::lineTotal)
                .reduce(Money::add)
                .orElseThrow(failBecauseOrderDoesntContainProduct());
    }

    @Override
    public void clearEvents() {
        events.clear();
    }

    @Override
    public List<DomainEvent> events() {
        return Collections.unmodifiableList(events);
    }

    private Supplier<IllegalStateException> failBecauseOrderDoesntContainProduct() {
        return () -> new IllegalStateException("Order must contain at least one product");
    }

    private void markPaymentPending() {
        if (!status.canTransitionTo(OrderStatus.PAYMENT_PENDING)) {
            throw new IllegalStateException("Cannot mark order as payment pending from status " + status);
        }
        this.status = OrderStatus.PAYMENT_PENDING;
    }

    private void markPaymentFailed() {
        if (!status.canTransitionTo(OrderStatus.PAYMENT_FAILED)) {
            throw new IllegalStateException("Cannot mark order as payment failed from status " + status);
        }
        this.status = OrderStatus.PAYMENT_FAILED;
    }

    private void markPaymentCanceled() {
        if (!status.canTransitionTo(OrderStatus.PAYMENT_CANCELED)) {
            throw new IllegalStateException("Cannot mark order as payment canceled from status " + status);
        }
        this.status = OrderStatus.PAYMENT_CANCELED;
    }

    private void markAsPaid(final CustomerId customer) {
        if (this.customer.equals(customer)) throw new IllegalArgumentException("Payment failed: the provided customer ID does not match the order's customer");

        if (!status.canTransitionTo(OrderStatus.PAID)) throw new IllegalStateException("Cannot mark as paid");

        this.status = OrderStatus.PAID;
        events.add(OrderPaid.record(customer, id));
    }
}
