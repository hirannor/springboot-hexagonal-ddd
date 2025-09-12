package hu.hirannor.hexagonal.domain.order;

import hu.hirannor.hexagonal.domain.customer.CustomerId;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.product.Product;
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
    private final Set<Product> products;
    private final CustomerId customer;
    private final Instant createdAt;
    private final List<DomainEvent> events;

    Order(final OrderId id, final Set<Product> products, final OrderStatus status, final CustomerId customer) {
        Objects.requireNonNull(id, "OrderId cannot be null");
        Objects.requireNonNull(products, "Ordered products cannot be null");
        Objects.requireNonNull(status, "OrderStatus cannot be null");

        this.id = id;
        this.products = products;
        this.status = status;
        this.createdAt = Instant.now();
        this.customer = customer;
        this.events = new ArrayList<>();
    }

    public static Order create(final MakeOrder command) {
        Objects.requireNonNull(command, "MakeOrder command cannot be null");

        if (command.products().isEmpty()) throw new IllegalArgumentException("Order must have at least one product");

        final Order createdOrder = new Order(
            OrderId.generate(),
            command.products(),
            OrderStatus.CREATED,
            command.customer()
        );

        createdOrder.events.add(OrderCreated.create(createdOrder.id, command.customer()));

        return createdOrder;
    }

    public OrderId id() {
        return id;
    }

    public OrderStatus status() {
        return status;
    }

    public Set<Product> products() {
        return Collections.unmodifiableSet(products);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public CustomerId customer() {
        return customer;
    }

    public void markAsPaid() {
        if (status != OrderStatus.CREATED) throw new IllegalStateException("Cannot mark as paid");

        this.status = OrderStatus.PAID;
        events.add(OrderPaid.create(id));
    }

    public Money totalPrice() {
        return products.stream()
                .map(Product::price)
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
}
