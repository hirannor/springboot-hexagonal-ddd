package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.basket;


import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;

import java.math.BigDecimal;

public record BasketItemModel(
        String productId,
        int quantity,
        BigDecimal priceAmount,
        CurrencyModel currency
) {
}
