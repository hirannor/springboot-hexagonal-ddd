package io.github.hirannor.oms.domain.payment;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;

public class PaymentBuilder {
    private PaymentId id;
    private OrderId orderId;
    private Money amount;
    private PaymentStatus status;
    private String providerReference;

    public PaymentBuilder id(final PaymentId id) {
        this.id = id;
        return this;
    }

    public PaymentBuilder order(final OrderId orderId) {
        this.orderId = orderId;
        return this;
    }

    public PaymentBuilder amount(final Money amount) {
        this.amount = amount;
        return this;
    }

    public PaymentBuilder status(final PaymentStatus status) {
        this.status = status;
        return this;
    }

    public PaymentBuilder providerReference(final String providerReference) {
        this.providerReference = providerReference;
        return this;
    }

    public Payment assemble() {
        return new Payment(id, orderId, amount, status, providerReference);
    }
}