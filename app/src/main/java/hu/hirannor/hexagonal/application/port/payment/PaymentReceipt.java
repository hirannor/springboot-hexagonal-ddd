package hu.hirannor.hexagonal.application.port.payment;

import java.util.Objects;

import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;

public record PaymentReceipt(
        OrderId orderId,
        PaymentStatus status,
        String providerReference,
        Money amount
) {
    public PaymentReceipt {
        Objects.requireNonNull(orderId);
        Objects.requireNonNull(status);
        Objects.requireNonNull(providerReference);
        Objects.requireNonNull(amount);
    }

    public static PaymentReceipt create(
            final OrderId orderId,
            final PaymentStatus status,
            final String providerReference,
            final Money amount
    ) {
        return new PaymentReceipt(orderId, status, providerReference, amount);
    }
}
