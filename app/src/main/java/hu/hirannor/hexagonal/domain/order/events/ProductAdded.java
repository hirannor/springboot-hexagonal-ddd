package hu.hirannor.hexagonal.domain.order.events;

import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.product.ProductId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record ProductAdded(EventId id,
                           OrderId orderId,
                           ProductId productId,
                           int quantity,
                           Money price) implements DomainEvent {

    public static ProductAdded record(final OrderId orderId,
                                        final ProductId productId,
                                        final int quantity,
                                        final Money price) {
        return new ProductAdded(EventId.generate(), orderId, productId, quantity, price);
    }

}
