package io.github.hirannor.oms.adapter.persistence.jpa.payment;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "EC_PAYMENTS")
public class PaymentModel {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "payments_seq"
    )
    @SequenceGenerator(
            name = "payments_seq",
            sequenceName = "payments_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @Column(name = "PAYMENT_ID", nullable = false, unique = true)
    private String paymentId;

    @Column(name = "ORDER_ID", nullable = false)
    private String orderId;

    @Column(name = "AMOUNT", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "CURRENCY", nullable = false, length = 3)
    private CurrencyModel currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 50)
    private PaymentStatusModel status;

    @Column(name = "PROVIDER_REFERENCE", nullable = false, unique = true, length = 255)
    private String providerReference;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    public PaymentModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(final String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(final String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public CurrencyModel getCurrency() {
        return currency;
    }

    public void setCurrency(final CurrencyModel currency) {
        this.currency = currency;
    }

    public PaymentStatusModel getStatus() {
        return status;
    }

    public void setStatus(final PaymentStatusModel status) {
        this.status = status;
    }

    public String getProviderReference() {
        return providerReference;
    }

    public void setProviderReference(final String providerReference) {
        this.providerReference = providerReference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }
}
