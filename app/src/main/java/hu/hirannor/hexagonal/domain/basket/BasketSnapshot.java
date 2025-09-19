package hu.hirannor.hexagonal.domain.basket;

import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import java.util.Collections;
import java.util.List;

public record BasketSnapshot(
        CustomerId customer,
        List<BasketItem> items) {

    public BasketSnapshot {
        items = Collections.unmodifiableList(items);
    }
}
