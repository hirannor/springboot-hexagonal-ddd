package hu.hirannor.hexagonal.domain.order.command;

import hu.hirannor.hexagonal.domain.core.valueobject.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;

public record PaymentInstruction(OrderId orderId, Money amount, String providerReference, String paymentUrl) {
    public static PaymentInstruction create(
            final OrderId orderId,
            final Money amount,
            final String providerReference,
            final String paymentUrl) {
        return new PaymentInstruction(orderId, amount, providerReference, paymentUrl);
    }
}
