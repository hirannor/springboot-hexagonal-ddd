package io.github.hirannor.oms.domain.product;


import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record ProductCreated(
        MessageId id,
        ProductId productId,
        String name,
        String description,
        Money price
) implements DomainEvent {

    public static ProductCreated record(final ProductId productId, final String name, final String description, final Money price) {
        return new ProductCreated(Message.generateId(), productId, name, description, price);
    }
}
