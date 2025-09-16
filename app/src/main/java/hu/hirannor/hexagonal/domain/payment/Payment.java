package hu.hirannor.hexagonal.domain.payment;

import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.payment.command.StartPayment;
import hu.hirannor.hexagonal.domain.payment.events.PaymentCanceled;
import hu.hirannor.hexagonal.domain.payment.events.PaymentFailed;
import hu.hirannor.hexagonal.domain.payment.events.PaymentInitialized;
import hu.hirannor.hexagonal.domain.payment.events.PaymentSucceeded;
import hu.hirannor.hexagonal.infrastructure.aggregate.AggregateRoot;
import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static hu.hirannor.hexagonal.domain.payment.PaymentStatus.*;

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
        this.id = Objects.requireNonNull(id);
        this.orderId = Objects.requireNonNull(orderId);
        this.amount = Objects.requireNonNull(amount);
        this.status = Objects.requireNonNull(status);
        this.providerReference = providerReference;
        this.createdAt = Instant.now();
        this.events = new ArrayList<>();
    }

    public PaymentId id() { return id; }
    public OrderId orderId() { return orderId; }
    public Money amount() { return amount; }
    public PaymentStatus status() { return status; }
    public String providerReference() { return providerReference; }
    public Instant createdAt() { return createdAt; }

    public static Payment start(final StartPayment start) {
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
        switch (receipt.status()) {
            case SUCCEEDED -> succeed();
            case FAILED -> fail();
            case CANCELED -> cancel();
        }
    }

    public void succeed() {
        if (!status.canTransitionTo(SUCCEEDED)) {
            throw new IllegalStateException("Cannot succeed from " + status);
        }
        this.status = SUCCEEDED;
        events.add(PaymentSucceeded.record(id, orderId, amount));
    }

    public void fail() {
        if (!status.canTransitionTo(FAILED)) {
            throw new IllegalStateException("Cannot fail from " + status);
        }
        this.status = FAILED;
        events.add(PaymentFailed.record(id, orderId));
    }

    public void cancel() {
        if (!status.canTransitionTo(CANCELED)) {
            throw new IllegalStateException("Cannot cancel from " + status);
        }
        this.status = CANCELED;
        events.add(PaymentCanceled.record(id, orderId));
    }

    @Override
    public List<DomainEvent> events() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public void clearEvents() {
        events.clear();
    }
}
