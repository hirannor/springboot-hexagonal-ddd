package hu.hirannor.hexagonal.domain.basket;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

import java.time.Instant;
import java.util.Set;

public record BasketCheckedOut(
        EventId id,
        CustomerId customerId,
        Set<OrderedProduct> products,
        Instant occurredAt
) implements DomainEvent {

    public static BasketCheckedOut record(final CustomerId customerId, final Set<OrderedProduct> products) {
        return new BasketCheckedOut(EventId.generate(), customerId, Set.copyOf(products), Instant.now());
    }
}
