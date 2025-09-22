package io.github.hirannor.oms.domain.payment.events;

import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record PaymentFailed(MessageId id, PaymentId paymentId, OrderId orderId) implements DomainEvent {
    public static PaymentFailed record(final PaymentId paymentId, final OrderId orderId) {
        return new PaymentFailed(Message.generateId(), paymentId, orderId);
    }

    public static PaymentFailed recreate(final MessageId id, final PaymentId paymentId, final OrderId orderId) {
        return new PaymentFailed(id, paymentId, orderId);
    }
}