package hu.hirannor.hexagonal.domain.basket;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;

import java.util.Collections;
import java.util.Set;

public record BasketSnapshot(
        CustomerId customer,
        Set<OrderedProduct> products) {

    public BasketSnapshot {
        products = Collections.unmodifiableSet(products);
    }
}
