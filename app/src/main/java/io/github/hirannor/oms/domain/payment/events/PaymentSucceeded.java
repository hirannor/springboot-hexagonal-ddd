package io.github.hirannor.oms.domain.payment.events;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record PaymentSucceeded(MessageId id, PaymentId paymentId, OrderId orderId, Money money) implements DomainEvent {
    public static PaymentSucceeded record(final PaymentId paymentId, final OrderId orderId, final Money amount) {
        return new PaymentSucceeded(Message.generateId(), paymentId, orderId, amount);
    }

    public static PaymentSucceeded recreate(final MessageId id, final PaymentId paymentId, final OrderId orderId, final Money amount) {
        return new PaymentSucceeded(id, paymentId, orderId, amount);
    }
}