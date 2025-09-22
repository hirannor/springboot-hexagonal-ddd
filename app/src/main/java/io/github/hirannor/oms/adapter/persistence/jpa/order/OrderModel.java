package io.github.hirannor.oms.adapter.persistence.jpa.order;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EC_ORDERS")
public class OrderModel {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "orders_seq"
    )
    @SequenceGenerator(
            name = "orders_seq",
            sequenceName = "orders_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @Column(name = "ORDER_ID", nullable = false, unique = true)
    private String orderId;

    @Column(name = "CUSTOMER_ID", nullable = false)
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 50)
    private OrderStatusModel status;

    @Column(name = "TOTAL_PRICE_AMOUNT", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPriceAmount;

    @Column(name = "TOTAL_PRICE_CURRENCY", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private CurrencyModel totalPriceCurrency;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItemsModel> orderItems = new ArrayList<>();

    public OrderModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(final String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final String customerId) {
        this.customerId = customerId;
    }

    public OrderStatusModel getStatus() {
        return status;
    }

    public void setStatus(final OrderStatusModel status) {
        this.status = status;
    }

    public BigDecimal getTotalPriceAmount() {
        return totalPriceAmount;
    }

    public void setTotalPriceAmount(final BigDecimal totalPriceAmount) {
        this.totalPriceAmount = totalPriceAmount;
    }

    public CurrencyModel getTotalPriceCurrency() {
        return totalPriceCurrency;
    }

    public void setTotalPriceCurrency(final CurrencyModel totalPriceCurrency) {
        this.totalPriceCurrency = totalPriceCurrency;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemsModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(final List<OrderItemsModel> orderItems) {
        this.orderItems.clear();
        if (orderItems != null) {
            orderItems.forEach(this::addProduct);
        }
    }

    public void addProduct(final OrderItemsModel orderItem) {
        orderItem.setOrder(this);
        this.orderItems.add(orderItem);
    }
}
