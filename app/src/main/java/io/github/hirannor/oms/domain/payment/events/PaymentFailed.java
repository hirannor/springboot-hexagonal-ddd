package io.github.hirannor.oms.domain.payment.events;

import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.event.EventId;

public record PaymentFailed(EventId id, PaymentId paymentId, OrderId orderId) implements DomainEvent {
    public static PaymentFailed record(final PaymentId paymentId, final OrderId orderId) {
        return new PaymentFailed(EventId.generate(),  paymentId, orderId);
    }
}