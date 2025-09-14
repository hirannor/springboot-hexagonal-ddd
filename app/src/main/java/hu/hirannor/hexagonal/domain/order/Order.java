package hu.hirannor.hexagonal.domain.order;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;
import hu.hirannor.hexagonal.domain.order.events.*;
import hu.hirannor.hexagonal.domain.order.payment.PaymentReceipt;
import hu.hirannor.hexagonal.domain.order.payment.PaymentTransaction;
import hu.hirannor.hexagonal.infrastructure.aggregate.AggregateRoot;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;

import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

public class Order extends AggregateRoot {

    private final OrderId id;
    private OrderStatus status;
    private final Set<OrderedProduct> orderedProducts;
    private final CustomerId customer;
    private final Instant createdAt;
    private final List<PaymentTransaction> transactions;
    private final List<OrderStatusChange> history;
    private final List<DomainEvent> events;

    Order(final OrderId id,
          final Set<OrderedProduct> orderedProducts,
          final OrderStatus status,
          final CustomerId customer,
          final List<PaymentTransaction> transactions) {
        Objects.requireNonNull(id, "OrderId cannot be null");
        Objects.requireNonNull(orderedProducts, "Ordered products cannot be null");
        Objects.requireNonNull(status, "OrderStatus cannot be null");

        this.id = id;
        this.orderedProducts = orderedProducts;
        this.status = status;
        this.createdAt = Instant.now();
        this.customer = customer;
        this.transactions = transactions;
        this.history = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public static OrderBuilder empty() {
        return new OrderBuilder();
    }

    public static Order create(final CreateOrder command) {
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
        final PaymentTransaction transaction = PaymentTransaction.from(receipt);
        transactions.add(transaction);

        switch (receipt.status()) {
            case SUCCESS -> markPaymentAsPaid();
            case PENDING -> markPaymentAsPending();
            case CANCELLED -> markPaymentAsCanceled();
            case FAILURE -> markPaymentAsFailed();
            default -> throw new IllegalStateException("Unknown payment status: " + receipt.status());
        }
    }

    public void changeStatus(final OrderStatus target) {
        if (!status.canTransitionTo(target)) {
            throw new IllegalStateException("Cannot change to " + target +  " status");
        }

        this.history.add(OrderStatusChange.from(status, target, Instant.now()));

        switch (target) {
            case SHIPPED -> ship();
            case DELIVERED -> deliver();
            case CANCELLED -> cancel();
            case RETURNED -> returnOrder();
            case REFUNDED -> refund();
            case PROCESSING -> startProcessing();
            default -> this.status = target;
        }
    }

    public void initializePayment() {
        if (!status.canTransitionTo(OrderStatus.PAYMENT_PENDING)) {
            throw new IllegalStateException("Cannot start processing from status " + status);
        }
        addHistory(status, OrderStatus.PAYMENT_PENDING);

        this.status = OrderStatus.PAYMENT_PENDING;
        events.add(OrderPaymentPending.record(id, customer));
    }

    public void startProcessing() {
        if (!status.canTransitionTo(OrderStatus.PROCESSING)) {
            throw new IllegalStateException("Cannot start processing from status " + status);
        }
        addHistory(status, OrderStatus.PROCESSING);

        this.status = OrderStatus.PROCESSING;
        events.add(OrderProcessing.record(id, customer));
    }

    public void ship() {
        if (!status.canTransitionTo(OrderStatus.SHIPPED)) {
            throw new IllegalStateException("Cannot ship order from status " + status);
        }
        addHistory(status, OrderStatus.SHIPPED);

        this.status = OrderStatus.SHIPPED;
        events.add(OrderShipped.record(id, customer));
    }

    public void deliver() {
        if (!status.canTransitionTo(OrderStatus.DELIVERED)) {
            throw new IllegalStateException("Cannot deliver order from status " + status);
        }
        addHistory(status, OrderStatus.DELIVERED);

        this.status = OrderStatus.DELIVERED;
        events.add(OrderDelivered.record(id, customer));
    }

    public void cancel() {
        if (!status.canTransitionTo(OrderStatus.CANCELLED)) {
            throw new IllegalStateException("Cannot cancel order from status " + status);
        }
        addHistory(status, OrderStatus.CANCELLED);

        this.status = OrderStatus.CANCELLED;
        events.add(OrderCanceled.record(id, customer));
    }

    public void returnOrder() {
        if (!status.canTransitionTo(OrderStatus.RETURNED)) {
            throw new IllegalStateException("Cannot return order from status " + status);
        }
        addHistory(status, OrderStatus.RETURNED);

        this.status = OrderStatus.RETURNED;
        events.add(OrderReturned.record(id, customer));
    }

    public void refund() {
        if (!status.canTransitionTo(OrderStatus.REFUNDED)) {
            throw new IllegalStateException("Cannot refund order from status " + status);
        }
        addHistory(status, OrderStatus.REFUNDED);

        this.status = OrderStatus.REFUNDED;
        events.add(OrderRefunded.record(id, customer));
    }

    public Money totalPrice() {
        return orderedProducts.stream()
                .map(OrderedProduct::lineTotal)
                .reduce(Money::add)
                .orElseThrow(failBecauseOrderDoesntContainProduct());
    }

    public List<PaymentTransaction> transactions() { return Collections.unmodifiableList(transactions); }

    public List<OrderStatusChange> history() { return Collections.unmodifiableList(history); }

    @Override
    public void clearEvents() {
        events.clear();
    }

    @Override
    public List<DomainEvent> events() {
        return Collections.unmodifiableList(events);
    }

    private void addHistory(final OrderStatus from, final OrderStatus to) {
        this.history.add(OrderStatusChange.from(from, to, Instant.now()));
    }

    private Supplier<IllegalStateException> failBecauseOrderDoesntContainProduct() {
        return () -> new IllegalStateException("Order must contain at least one product");
    }

    private void markPaymentAsPaid() {
        changeStatus(OrderStatus.PAID_SUCCESSFULLY);
        this.events.add(OrderPaid.record(customer, id));
    }
    private void markPaymentAsPending() {
        changeStatus(OrderStatus.PAYMENT_PENDING);
        this.events.add(OrderPaymentPending.record(id, customer));
    }
    private void markPaymentAsCanceled() {
        changeStatus(OrderStatus.PAYMENT_CANCELED);
        this.events.add(OrderPaymentCanceled.record(customer, id));
    }
    private void markPaymentAsFailed() {
        changeStatus(OrderStatus.PAYMENT_FAILED);
        this.events.add(OrderPaymentFailed.record(customer, id));
    }

}
