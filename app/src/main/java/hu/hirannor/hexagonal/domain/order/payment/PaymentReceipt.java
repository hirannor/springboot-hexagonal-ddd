package hu.hirannor.hexagonal.domain.order.payment;

import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;

import java.util.Objects;

public record PaymentReceipt(
        String transactionId,
        PaymentMethod paymentMethod,
        OrderId orderId,
        PaymentStatus status,
        String providerReference,
        Money amount
) {
    public PaymentReceipt {
        Objects.requireNonNull(transactionId);
        Objects.requireNonNull(paymentMethod);
        Objects.requireNonNull(orderId);
        Objects.requireNonNull(status);
        Objects.requireNonNull(providerReference);
        Objects.requireNonNull(amount);
    }

    public static PaymentReceipt create(
            final String transactionId,
            final PaymentMethod paymentMethod,
            final OrderId orderId,
            final PaymentStatus status,
            final String providerReference,
            final Money amount
    ) {
        return new PaymentReceipt(transactionId, paymentMethod, orderId, status, providerReference, amount);
    }
}
