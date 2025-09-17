package hu.hirannor.hexagonal.adapter.web.rest.order.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.MoneyModel;
import hu.hirannor.hexagonal.adapter.web.rest.orders.model.OrderedProductModel;
import hu.hirannor.hexagonal.domain.core.valueobject.Money;
import hu.hirannor.hexagonal.domain.order.OrderItem;
import hu.hirannor.hexagonal.domain.product.ProductId;

import java.util.function.Function;

public class OrderedProductModelToDomainMapper implements Function<OrderedProductModel, OrderItem> {

    private final Function<MoneyModel, Money> mapMoneyModelToDomain;

    public OrderedProductModelToDomainMapper() {
        this.mapMoneyModelToDomain = new MoneyModelToDomainMapper();
    }

    @Override
    public OrderItem apply(final OrderedProductModel model) {
        if (model == null) return null;

        return OrderItem.create(
                ProductId.from(model.getProductId()),
                model.getQuantity(),
                mapMoneyModelToDomain.apply(model.getPrice())
        );
    }
}
