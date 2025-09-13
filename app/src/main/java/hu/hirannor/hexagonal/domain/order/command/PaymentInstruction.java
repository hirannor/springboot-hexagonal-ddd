package hu.hirannor.hexagonal.domain.order.command;

import hu.hirannor.hexagonal.application.service.payment.PaymentStatus;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;

public record PaymentInstruction(OrderId orderId, Money amount, PaymentStatus status, String paymentUrl) {
    public static PaymentInstruction create(
            final OrderId orderId,
            final Money amount,
            final PaymentStatus status,
            final String paymentUrl) {
        return new PaymentInstruction(orderId, amount, status, paymentUrl);
    }
}
