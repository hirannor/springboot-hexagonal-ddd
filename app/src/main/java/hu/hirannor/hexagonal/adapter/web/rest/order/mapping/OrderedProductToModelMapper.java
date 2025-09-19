package hu.hirannor.hexagonal.adapter.web.rest.order.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.MoneyModel;
import hu.hirannor.hexagonal.adapter.web.rest.orders.model.OrderedProductModel;
import hu.hirannor.hexagonal.domain.core.valueobject.Money;
import hu.hirannor.hexagonal.domain.order.OrderItem;
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
