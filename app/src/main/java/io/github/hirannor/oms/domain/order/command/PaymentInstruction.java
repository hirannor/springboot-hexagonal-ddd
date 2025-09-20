package io.github.hirannor.oms.domain.order.command;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;

public record PaymentInstruction(OrderId orderId, Money price, String providerReference, String paymentUrl) {
    public static PaymentInstruction create(
            final OrderId orderId,
            final Money price,
            final String providerReference,
            final String paymentUrl) {
        return new PaymentInstruction(orderId, price, providerReference, paymentUrl);
    }
}
