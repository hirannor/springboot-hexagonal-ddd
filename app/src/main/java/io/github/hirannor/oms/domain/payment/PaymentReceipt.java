package io.github.hirannor.oms.domain.payment;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;

import java.util.Objects;

public record PaymentReceipt(
        String transactionId,
        String providerPaymentId,
        PaymentMethod paymentMethod,
        OrderId orderId,
        PaymentStatus status,
        String providerReference,
        Money amount
) {
    public PaymentReceipt {
        Objects.requireNonNull(transactionId);
        Objects.requireNonNull(providerPaymentId);
        Objects.requireNonNull(paymentMethod);
        Objects.requireNonNull(orderId);
        Objects.requireNonNull(status);
        Objects.requireNonNull(providerReference);
        Objects.requireNonNull(amount);
    }

    public static PaymentReceipt create(
            final String transactionId,
            final String providerPaymentId,
            final PaymentMethod paymentMethod,
            final OrderId orderId,
            final PaymentStatus status,
            final String providerReference,
            final Money amount
    ) {
        return new PaymentReceipt(transactionId, providerPaymentId, paymentMethod, orderId, status, providerReference, amount);
    }
}
