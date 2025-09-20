package io.github.hirannor.oms.adapter.web.rest.order.mapping;

import io.github.hirannor.oms.adapter.web.rest.orders.model.MoneyModel;
import io.github.hirannor.oms.adapter.web.rest.orders.model.OrderedProductModel;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderItem;

import java.util.function.Function;

public class OrderedProductToModelMapper implements Function<OrderItem, OrderedProductModel> {

    private final Function<Money, MoneyModel> mapMoneyToModel;

    public OrderedProductToModelMapper() {
        this.mapMoneyToModel = new MoneyToModelMapper();
    }

    @Override
    public OrderedProductModel apply(final OrderItem domain) {
        if (domain == null) return null;

        return new OrderedProductModel()
                .productId(domain.productId().asText())
                .quantity(domain.quantity())
                .price(mapMoneyToModel.apply(domain.price()));
    }
}
