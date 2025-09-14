package hu.hirannor.hexagonal.domain.order.command;

import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;

public record PaymentInstruction(OrderId orderId, Money amount, String paymentUrl) {
    public static PaymentInstruction create(
            final OrderId orderId,
            final Money amount,
            final String paymentUrl) {
        return new PaymentInstruction(orderId, amount, paymentUrl);
    }
}
