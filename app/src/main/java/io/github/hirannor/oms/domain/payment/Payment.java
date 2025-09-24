package io.github.hirannor.oms.domain.payment;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.command.StartPayment;
import io.github.hirannor.oms.domain.payment.events.*;
import io.github.hirannor.oms.infrastructure.aggregate.AggregateRoot;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static io.github.hirannor.oms.domain.payment.PaymentStatus.*;

public class Payment extends AggregateRoot {

    private final PaymentId id;
    private final OrderId orderId;
    private final Money amount;
    private final Instant createdAt;
    private final List<DomainEvent> events;
    private final String providerReference;

    private PaymentStatus status;

    Payment(final PaymentId id,
            final OrderId orderId,
            final Money amount,
            final PaymentStatus status,
            final String providerReference) {

        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.orderId = Objects.requireNonNull(orderId, "orderId cannot be null");
        this.amount = Objects.requireNonNull(amount, "price cannot be null");
        this.status = Objects.requireNonNull(status, "status cannot be null");
        this.providerReference = Objects.requireNonNull(providerReference, "providerReference cannot be null");

        this.createdAt = Instant.now();
        this.events = new ArrayList<>();
    }

    public PaymentId id() {
        return id;
    }

    public OrderId orderId() {
        return orderId;
    }

    public Money amount() {
        return amount;
    }

    public PaymentStatus status() {
        return status;
    }

    public String providerReference() {
        return providerReference;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public static Payment start(final StartPayment start) {
        Objects.requireNonNull(start, "start command cannot be null");

        final Payment payment = new PaymentBuilder()
                .id(start.paymentId())
                .order(start.orderId())
                .amount(start.amount())
                .status(INITIALIZED)
                .providerReference(start.providerReference())
                .assemble();

        payment.events.add(PaymentInitialized.record(payment.id, start.orderId(), start.amount()));

        return payment;
    }

    public void applyReceipt(final PaymentReceipt receipt) {
        Objects.requireNonNull(receipt, "receipt cannot be null");

        switch (receipt.status()) {
            case SUCCEEDED -> {
                if (status != SUCCEEDED) succeed();
            }
            case FAILED -> {
                if (status != FAILED) fail();
            }
            case EXPIRED -> {
                if (status != EXPIRED) expire();
            }
            case CANCELED -> {
                if (status != CANCELED) cancel();
            }
        }
    }

    @Override
    public List<DomainEvent> events() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public void clearEvents() {
        events.clear();
    }

    private void succeed() {
        if (!status.canTransitionTo(SUCCEEDED))
            throw new IllegalStateException("Cannot succeed from " + status);

        this.status = SUCCEEDED;
        events.add(PaymentSucceeded.record(id, orderId, amount));
    }

    private void expire() {
        if (!status.canTransitionTo(EXPIRED))
            throw new IllegalStateException("Cannot succeed from " + status);

        this.status = EXPIRED;
        events.add(PaymentExpired.record(id, orderId));
    }

    private void fail() {
        if (!status.canTransitionTo(FAILED))
            throw new IllegalStateException("Cannot fail from " + status);

        this.status = FAILED;
        events.add(PaymentFailed.record(id, orderId));
    }

    private void cancel() {
        if (!status.canTransitionTo(CANCELED))
            throw new IllegalStateException("Cannot cancel from " + status);

        this.status = CANCELED;
        events.add(PaymentCanceled.record(id, orderId));
    }
}
