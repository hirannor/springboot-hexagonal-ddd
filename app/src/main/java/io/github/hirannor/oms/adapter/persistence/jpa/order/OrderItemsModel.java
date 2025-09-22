package io.github.hirannor.oms.adapter.persistence.jpa.order;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "EC_ORDERED_PRODUCTS")
public class OrderItemsModel {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ordered_product_seq"
    )
    @SequenceGenerator(
            name = "ordered_product_seq",
            sequenceName = "ordered_product_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private OrderModel order;

    @Column(name = "PRODUCT_ID", nullable = false)
    private String productId;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    @Column(name = "PRICE_AMOUNT", nullable = false)
    private BigDecimal priceAmount;

    @Column(name = "PRICE_CURRENCY", nullable = false)
    private CurrencyModel priceCurrency;

    public OrderItemsModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(BigDecimal priceAmount) {
        this.priceAmount = priceAmount;
    }

    public CurrencyModel getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(CurrencyModel priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

}
