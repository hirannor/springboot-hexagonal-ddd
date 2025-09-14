package hu.hirannor.hexagonal.adapter.web.rest.order;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.MoneyModel;
import hu.hirannor.hexagonal.adapter.web.rest.orders.model.OrderedProductModel;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;

import java.util.function.Function;

public class OrderedProductToModelMapper implements Function<OrderedProduct, OrderedProductModel> {

    private final Function<Money, MoneyModel> mapMoneyToModel;

    public OrderedProductToModelMapper() {
        this.mapMoneyToModel = new MoneyToModelMapper();
    }

    @Override
    public OrderedProductModel apply(final OrderedProduct domain) {
        if (domain == null) return null;

        return new OrderedProductModel()
                .productId(domain.productId().asText())
                .quantity(domain.quantity())
                .price(mapMoneyToModel.apply(domain.price()));
    }
}
