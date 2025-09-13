package hu.hirannor.hexagonal.application.service.payment;


import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record PaymentReceipt(
        UUID transactionId,
        PaymentStatus status,
        Instant processedAt,
        String providerReference
) {
    public PaymentReceipt {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(processedAt, "processedAt must not be null");
        Objects.requireNonNull(providerReference, "providerReference must not be null");
    }

    public static PaymentReceipt success(final String providerReference) {
        return new PaymentReceipt(UUID.randomUUID(), PaymentStatus.SUCCESS, Instant.now(), providerReference);
    }

    public static PaymentReceipt failure(final String providerReference) {
        return new PaymentReceipt(UUID.randomUUID(), PaymentStatus.FAILURE, Instant.now(), providerReference);
    }

    public static PaymentReceipt pending(final String providerReference) {
        return new PaymentReceipt(UUID.randomUUID(), PaymentStatus.PENDING, Instant.now(), providerReference);
    }

    public static PaymentReceipt cancelled(final String providerReference) {
        return new PaymentReceipt(UUID.randomUUID(), PaymentStatus.CANCELLED, Instant.now(), providerReference);
    }
}
