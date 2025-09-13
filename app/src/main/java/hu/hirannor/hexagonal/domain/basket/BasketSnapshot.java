package hu.hirannor.hexagonal.domain.basket;

import hu.hirannor.hexagonal.domain.CustomerId;

import java.util.Collections;
import java.util.Set;

public record BasketSnapshot(
        CustomerId customer,
        Set<BasketItem> items) {

    public BasketSnapshot {
        items = Collections.unmodifiableSet(items);
    }
}
