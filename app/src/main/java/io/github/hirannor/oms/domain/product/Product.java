package io.github.hirannor.oms.domain.product;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.infrastructure.aggregate.AggregateRoot;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Product extends AggregateRoot {
    private final ProductId id;
    private final String name;
    private final String description;
    private final Money price;
    private final List<DomainEvent> events;

    Product(final ProductId id,
            final String name,
            final String description,
            final Money price) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(description);
        Objects.requireNonNull(price);

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.events = new ArrayList<>(0);
    }

    public static ProductBuilder empty() {
        return new ProductBuilder();
    }

    public static Product create(final CreateProduct cmd) {
        Objects.requireNonNull(cmd);

        final Product product = empty()
                .id(cmd.productId())
                .name(cmd.name())
                .description(cmd.description())
                .price(cmd.price())
                .assemble();

        product.events.add(ProductCreated.record(
                product.id,
                product.name(),
                product.description(),
                product.price()
        ));

        return product;
    }

    public ProductId id() {
        return id;
    }

    public Money price() {
        return price;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    @Override
    public List<DomainEvent> events() {
        return events;
    }

    @Override
    public void clearEvents() {
        events.clear();
    }
}
