package hu.hirannor.hexagonal.domain.payment.events;

import hu.hirannor.hexagonal.domain.core.valueobject.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.payment.PaymentId;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import hu.hirannor.hexagonal.infrastructure.event.EventId;

public record PaymentSucceeded(EventId id, PaymentId paymentId, OrderId orderId, Money amount) implements DomainEvent {
    public static PaymentSucceeded record(final PaymentId paymentId, final OrderId orderId, final Money amount) {
        return new PaymentSucceeded(EventId.generate(),  paymentId, orderId, amount);
    }
}