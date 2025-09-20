package io.github.hirannor.oms.domain.payment.events;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.event.EventId;

public record PaymentInitialized(EventId id, PaymentId paymentId, OrderId orderId, Money amount) implements DomainEvent {
    public static PaymentInitialized record(final PaymentId paymentId, final OrderId orderId, final Money amount) {
        return new PaymentInitialized(EventId.generate(),  paymentId, orderId, amount);
    }
}