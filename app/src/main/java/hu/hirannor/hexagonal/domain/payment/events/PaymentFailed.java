package hu.hirannor.hexagonal.domain.payment.events;

import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.payment.PaymentId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record PaymentFailed(EventId id, PaymentId paymentId, OrderId orderId) implements DomainEvent {
    public static PaymentFailed record(final PaymentId paymentId, final OrderId orderId) {
        return new PaymentFailed(EventId.generate(),  paymentId, orderId);
    }
}