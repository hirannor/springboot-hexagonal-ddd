package io.github.hirannor.oms.domain.basket;

import io.github.hirannor.oms.domain.basket.command.CreateBasket;
import io.github.hirannor.oms.domain.basket.events.BasketCheckedOut;
import io.github.hirannor.oms.domain.basket.events.BasketCreated;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.infrastructure.aggregate.AggregateRoot;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Basket extends AggregateRoot {

    private final BasketId id;
    private final CustomerId customer;
    private BasketStatus status;
    private final List<BasketItem> items;
    private final List<DomainEvent> events;

    Basket(final BasketId id, final CustomerId customer) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(customer);

        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.events = new ArrayList<>();
        this.status = BasketStatus.ACTIVE;
    }

    Basket(final BasketId id, final CustomerId customer, final List<BasketItem> items, final BasketStatus status) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(customer);

        this.id = id;
        this.customer = customer;
        this.items = items;
        this.status = status;
        this.events = new ArrayList<>();
    }

    public static Basket from(final BasketId id,
                              final CustomerId customer,
                              final List<BasketItem> items,
                              final BasketStatus status) {
        return new Basket(id, customer, new ArrayList<>(items), status);
    }

    public static Basket create(final CreateBasket create) {
        Objects.requireNonNull(create, "CreateBasket command cannot be null");

        final Basket basket = new Basket(BasketId.generate(), create.customerId());

        basket.events.add(BasketCreated.record(create.basketId(), create.customerId()));

        return basket;
    }

    public BasketId id() {
        return id;
    }

    public CustomerId customer() {
        return customer;
    }

    public BasketStatus status() {
        return status;
    }

    public List<BasketItem> items() {
        return Collections.unmodifiableList(items);
    }

    public void addProduct(final BasketItem item) {
        items.add(item);
    }

    public void removeProduct(final BasketItem item) {
        items.remove(item);
    }

    public Money totalPrice() {
        return items.stream()
                .map(BasketItem::lineTotal)
                .reduce(Money::add)
                .orElseGet(() -> Money.zero(defaultCurrency()));
    }

    public BasketSnapshot snapshot() {
        return new BasketSnapshot(customer, items);
    }

    public boolean isCheckedOut() {
        return status == BasketStatus.CHECKED_OUT;
    }

    public void checkout() {
        if (status != BasketStatus.ACTIVE)
            throw new IllegalStateException("Basket cannot be checked out from status " + status);

        if (items.isEmpty())
            throw new IllegalStateException("Cannot checkout an empty basket");

        this.status = BasketStatus.CHECKED_OUT;
        events.add(BasketCheckedOut.record(customer, items));
    }

    @Override
    public void clearEvents() {
        events.clear();
    }

    @Override
    public List<DomainEvent> events() {
        return Collections.unmodifiableList(events);
    }

    private Currency defaultCurrency() {
        return items.stream()
                .findFirst()
                .map(extractPrice()
                        .andThen(Money::currency))
                .orElse(Currency.EUR);
    }

    private Function<BasketItem, Money> extractPrice() {
        return BasketItem::price;
    }
}
