package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.basket;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.currency.CurrencyModel;

import java.math.BigDecimal;

public class BasketItemModel {
    private String productId;
    private int quantity;
    private BigDecimal priceAmount;
    private CurrencyModel currency;

    public BasketItemModel() {
    }

    public BasketItemModel(final String productId, final int quantity, final BigDecimal priceAmount, final CurrencyModel currency) {
        this.productId = productId;
        this.quantity = quantity;
        this.priceAmount = priceAmount;
        this.currency = currency;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(final String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(final BigDecimal priceAmount) {
        this.priceAmount = priceAmount;
    }

    public CurrencyModel getCurrency() {
        return currency;
    }

    public void setCurrency(final CurrencyModel currency) {
        this.currency = currency;
    }
}
