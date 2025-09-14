package hu.hirannor.hexagonal.adapter.persistence.jpa;

import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.PaymentMethodModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.PaymentStatusModel;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "EC_ORDER_PAYMENTS")
public class PaymentTransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_tx_seq")
    @SequenceGenerator(name = "order_tx_seq", sequenceName = "order_tx_seq", allocationSize = 5)
    private Long id;

    @Column(name = "TRANSACTION_ID", nullable = false, unique = true)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private PaymentStatusModel status;

    @Column(name = "AMOUNT", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "CURRENCY", nullable = false, length = 3)
    private CurrencyModel currency;

    @Column(name = "PAYMENT_METHOD", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private PaymentMethodModel paymentMethod;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID_FK", nullable = false)
    private OrderModel order;

    public PaymentTransactionModel() {}

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentStatusModel getStatus() {
        return status;
    }

    public void setStatus(final PaymentStatusModel status) {
        this.status = status;
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

    public PaymentMethodModel getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(final PaymentMethodModel paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(final OrderModel order) {
        this.order = order;
    }
}