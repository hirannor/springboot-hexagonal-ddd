package hu.hirannor.hexagonal.domain.product;


import hu.hirannor.hexagonal.domain.core.valueobject.Money;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

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
