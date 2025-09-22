package io.github.hirannor.oms.domain.payment.events;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record PaymentInitialized(MessageId id, PaymentId paymentId, OrderId orderId,
                                 Money money) implements DomainEvent {
    public static PaymentInitialized record(final PaymentId paymentId, final OrderId orderId, final Money amount) {
        return new PaymentInitialized(Message.generateId(), paymentId, orderId, amount);
    }

    public static PaymentInitialized recreate(final MessageId id, final PaymentId paymentId, final OrderId orderId, final Money amount) {
        return new PaymentInitialized(id, paymentId, orderId, amount);
    }
}