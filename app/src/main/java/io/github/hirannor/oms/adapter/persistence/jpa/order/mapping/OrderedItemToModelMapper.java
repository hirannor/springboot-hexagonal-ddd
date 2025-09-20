package io.github.hirannor.oms.adapter.persistence.jpa.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.order.OrderItemsModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.order.OrderItem;

import java.util.function.Function;

public class OrderedItemToModelMapper implements Function<OrderItem, OrderItemsModel> {

    private final Function<Currency, CurrencyModel> mapCurrencyToModel;

    public OrderedItemToModelMapper() {
        this.mapCurrencyToModel = new CurrencyToModelMapper();
    }

    @Override
    public OrderItemsModel apply(final OrderItem domain) {
        if (domain == null) return null;

        final OrderItemsModel model = new OrderItemsModel();
        model.setProductId(domain.productId().asText());
        model.setQuantity(domain.quantity());
        model.setPriceAmount(domain.price().amount());
        model.setPriceCurrency(mapCurrencyToModel.apply(domain.price().currency()));

        return model;
    }
}
