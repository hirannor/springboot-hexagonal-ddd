package io.github.hirannor.oms.adapter.persistence.jpa.order.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.order.OrderItemsModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderItem;
import io.github.hirannor.oms.domain.product.ProductId;

import java.util.function.Function;

public class OrderedItemModelToDomainMapper implements Function<OrderItemsModel, OrderItem> {

    private final Function<CurrencyModel, Currency> mapCurrencyModelToDomain;

    public OrderedItemModelToDomainMapper() {
        this.mapCurrencyModelToDomain = new CurrencyModelToDomainMapper();
    }

    @Override
    public OrderItem apply(final OrderItemsModel model) {
        if (model == null) return null;

        final Currency currency = mapCurrencyModelToDomain.apply(model.getPriceCurrency());

        return OrderItem.create(
                ProductId.from(model.getProductId()),
                model.getQuantity(),
                Money.of(
                        model.getPriceAmount(),
                        currency
                ));
    }
}
