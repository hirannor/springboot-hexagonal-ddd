package hu.hirannor.hexagonal.domain.payment.events;

import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.payment.PaymentId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record PaymentCanceled(EventId id, PaymentId paymentId, OrderId orderId) implements DomainEvent {
    public static PaymentCanceled record(final PaymentId paymentId, final OrderId orderId) {
        return new PaymentCanceled(EventId.generate(),  paymentId, orderId);
    }
}