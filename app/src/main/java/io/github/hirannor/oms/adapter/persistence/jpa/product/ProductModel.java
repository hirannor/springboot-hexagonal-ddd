package io.github.hirannor.oms.adapter.persistence.jpa.product;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "EC_PRODUCT")
public class ProductModel {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_seq"
    )
    @SequenceGenerator(
            name = "product_seq",
            sequenceName = "product_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE_AMOUNT")
    private BigDecimal priceAmount;

    @Column(name = "PRICE_CURRENCY")
    @Enumerated(EnumType.STRING)
    private CurrencyModel priceCurrency;

    public ProductModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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