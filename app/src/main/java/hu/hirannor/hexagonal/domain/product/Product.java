package hu.hirannor.hexagonal.domain.product;

import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.infrastructure.aggregate.AggregateRoot;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;

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

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.events = new ArrayList<>(0);
    }

    public static Product create(final CreateProduct cmd) {
        Objects.requireNonNull(cmd);

        final Product product = new Product(
            cmd.productId(),
            cmd.name(),
            cmd.description(),
            cmd.price()
        );

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
