package hu.hirannor.hexagonal.domain.basket;

import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;
import hu.hirannor.hexagonal.infrastructure.aggregate.AggregateRoot;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;

import java.util.*;
import java.util.function.Function;

public class Basket extends AggregateRoot {

    private final CustomerId customer;
    private final Set<OrderedProduct> products;
    private final List<DomainEvent> events;

    public Basket(final CustomerId customer) {
        this.customer = customer;
        this.products = new HashSet<>();
        this.events = new ArrayList<>();
    }

    public CustomerId customer() {
        return customer;
    }

    public Set<OrderedProduct> products() {
        return Collections.unmodifiableSet(products);
    }

    public void addProduct(final OrderedProduct product) {
        products.add(product);
    }

    public void removeProduct(final OrderedProduct product) {
        products.remove(product);
    }

    public Money totalPrice() {
        return products.stream()
                .map(OrderedProduct::lineTotal)
                .reduce(Money::add)
                .orElseGet(() -> Money.zero(defaultCurrency()));
    }

    public BasketSnapshot snapshot() {
        return new BasketSnapshot(customer, products);
    }

    public void checkout() {
        events.add(BasketCheckedOut.record(customer, products));
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
        return products.stream()
                .findFirst()
                .map(extractPrice()
                        .andThen(Money::currency))
                .orElse(Currency.EUR);
    }

    private Function<OrderedProduct, Money> extractPrice() {
        return OrderedProduct::price;
    }
}
