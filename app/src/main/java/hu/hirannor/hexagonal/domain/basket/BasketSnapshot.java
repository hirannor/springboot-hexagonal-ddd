package hu.hirannor.hexagonal.domain.basket;

import hu.hirannor.hexagonal.domain.CustomerId;

import java.util.*;

public record BasketSnapshot(
        CustomerId customer,
        List<BasketItem> items) {

    public BasketSnapshot {
        items = Collections.unmodifiableList(items);
    }
}
