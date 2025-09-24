package io.github.hirannor.oms.domain.payment.events;

import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record PaymentExpired(MessageId id, PaymentId paymentId, OrderId orderId) implements DomainEvent {
    public static PaymentExpired record(final PaymentId paymentId, final OrderId orderId) {
        return new PaymentExpired(Message.generateId(), paymentId, orderId);
    }

    public static PaymentExpired recreate(final MessageId id, final PaymentId paymentId, final OrderId orderId) {
        return new PaymentExpired(id, paymentId, orderId);
    }
}