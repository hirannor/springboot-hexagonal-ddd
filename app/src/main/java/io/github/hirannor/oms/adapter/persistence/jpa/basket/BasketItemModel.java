package io.github.hirannor.oms.adapter.persistence.jpa.basket;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "EC_BASKET_ITEMS")
public class BasketItemModel {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "basket_item_seq"
    )
    @SequenceGenerator(
            name = "basket_item_seq",
            sequenceName = "basket_item_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BASKET_ID_FK", nullable = false)
    private BasketModel basket;

    @Column(name = "PRODUCT_ID", nullable = false)
    private String productId;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    @Column(name = "PRICE_AMOUNT", nullable = false)
    private BigDecimal priceAmount;

    @Column(name = "PRICE_CURRENCY", nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyModel priceCurrency;

    public BasketItemModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BasketModel getBasket() {
        return basket;
    }

    public void setBasket(BasketModel basket) {
        this.basket = basket;
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
