package hu.hirannor.hexagonal.adapter.web.rest.order;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.MoneyModel;
import hu.hirannor.hexagonal.adapter.web.rest.orders.model.OrderedProductModel;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;
import hu.hirannor.hexagonal.domain.product.ProductId;

import java.util.function.Function;

public class OrderedProductModelToDomainMapper implements Function<OrderedProductModel, OrderedProduct> {

    private final Function<MoneyModel, Money> mapMoneyModelToDomain;

    public OrderedProductModelToDomainMapper() {
        this.mapMoneyModelToDomain = new MoneyModelToDomainMapper();
    }

    @Override
    public OrderedProduct apply(final OrderedProductModel model) {
        if (model == null) return null;

        return OrderedProduct.create(
                ProductId.from(model.getProductId()),
                model.getQuantity(),
                mapMoneyModelToDomain.apply(model.getPrice())
        );
    }
}
