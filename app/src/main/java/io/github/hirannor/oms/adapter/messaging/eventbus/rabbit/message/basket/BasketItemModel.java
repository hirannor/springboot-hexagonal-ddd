package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.basket;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.currency.CurrencyModel;

import java.math.BigDecimal;

public record BasketItemModel(
        String productId,
        int quantity,
        BigDecimal priceAmount,
        CurrencyModel currency
) {
}
