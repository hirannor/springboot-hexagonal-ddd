package io.github.hirannor.oms.domain.payment.events;

import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.event.EventId;

public record PaymentCanceled(EventId id, PaymentId paymentId, OrderId orderId) implements DomainEvent {
    public static PaymentCanceled record(final PaymentId paymentId, final OrderId orderId) {
        return new PaymentCanceled(EventId.generate(),  paymentId, orderId);
    }
}