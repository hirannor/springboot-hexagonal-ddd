package hu.hirannor.hexagonal.domain.order.payment;

import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;

import java.time.Instant;
import java.util.Objects;

public record PaymentTransaction(
        String transactionId,
        PaymentMethod paymentMethod,
        OrderId orderId,
        PaymentStatus status,
        Money amount,
        Instant createdAt
) {

    public PaymentTransaction {
        Objects.requireNonNull(transactionId);
        Objects.requireNonNull(paymentMethod);
        Objects.requireNonNull(orderId);
        Objects.requireNonNull(status);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(createdAt);
    }

    public static PaymentTransaction from(final PaymentReceipt receipt) {
        return new PaymentTransaction(
                receipt.transactionId(),
                receipt.paymentMethod(),
                receipt.orderId(),
                receipt.status(),
                receipt.amount(),
                Instant.now()
        );
    }

    public static PaymentTransaction create(
            final String transactionId,
            final PaymentMethod paymentMethod,
            final OrderId orderId,
            final PaymentStatus status,
            final Money amount) {
        return new PaymentTransaction(
                transactionId,
                paymentMethod,
                orderId,
                status,
                amount,
                Instant.now()
        );
    }
}
