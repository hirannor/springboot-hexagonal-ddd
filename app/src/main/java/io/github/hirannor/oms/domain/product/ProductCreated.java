package io.github.hirannor.oms.domain.product;


import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.event.EventId;

public record ProductCreated(
        EventId id,
        ProductId productId,
        String name,
        String description,
        Money price
) implements DomainEvent {

    public static ProductCreated record(final ProductId productId, final String name, final String description, final Money price) {
        return new ProductCreated(EventId.generate(), productId, name, description, price);
    }
}
