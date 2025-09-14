package hu.hirannor.hexagonal.domain.order.payment;

import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record PaymentTransaction(
        String transactionId,
        OrderId orderId,
        PaymentStatus status,
        Money amount,
        String providerReference,
        Instant createdAt
) {

    public PaymentTransaction {
        Objects.requireNonNull(transactionId);
        Objects.requireNonNull(orderId);
        Objects.requireNonNull(status);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(providerReference);
        Objects.requireNonNull(createdAt);
    }

    public static PaymentTransaction from(final PaymentReceipt receipt) {
        return new PaymentTransaction(
                UUID.randomUUID().toString(),
                receipt.orderId(),
                receipt.status(),
                receipt.amount(),
                receipt.providerReference(),
                Instant.now()
        );
    }

    public static PaymentTransaction create(
            final OrderId orderId,
            final PaymentStatus status,
            final Money amount,
            final String providerReference) {
        return new PaymentTransaction(
                UUID.randomUUID().toString(),
                orderId,
                status,
                amount,
                providerReference,
                Instant.now()
        );
    }
}
