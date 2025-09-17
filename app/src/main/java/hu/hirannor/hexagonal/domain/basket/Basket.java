package hu.hirannor.hexagonal.domain.basket;

import hu.hirannor.hexagonal.domain.core.valueobject.Currency;
import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.domain.core.valueobject.Money;
import hu.hirannor.hexagonal.domain.basket.command.CreateBasket;
import hu.hirannor.hexagonal.domain.basket.events.BasketCheckedOut;
import hu.hirannor.hexagonal.domain.basket.events.BasketCreated;
import hu.hirannor.hexagonal.infrastructure.aggregate.AggregateRoot;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Basket extends AggregateRoot {

    private final BasketId id;
    private final CustomerId customer;
    private final List<BasketItem> items;
    private final List<DomainEvent> events;

    Basket(final BasketId id, final CustomerId customer) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(customer);

        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    Basket(final BasketId id, final CustomerId customer,  final List<BasketItem> items) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(customer);

        this.id = id;
        this.customer = customer;
        this.items = items;
        this.events = new ArrayList<>();
    }

    public static Basket from(final BasketId id, final CustomerId customer, final List<BasketItem> items) {
        return new Basket(id, customer, new ArrayList<>(items));
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

    public void checkout() {
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
