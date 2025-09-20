package io.github.hirannor.oms.adapter.web.rest.order.mapping;

import io.github.hirannor.oms.adapter.web.rest.orders.model.CreateOrderModel;
import io.github.hirannor.oms.adapter.web.rest.orders.model.OrderedProductModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderItem;
import io.github.hirannor.oms.domain.order.command.CreateOrder;

import java.util.List;
import java.util.function.Function;

public class CreateOrderModelToCommandMapper implements Function<CreateOrderModel, CreateOrder> {

    private final Function<OrderedProductModel, OrderItem> mapModelToDomain;

    public CreateOrderModelToCommandMapper() {
        this.mapModelToDomain = new OrderedProductModelToDomainMapper();
    }

    @Override
    public CreateOrder apply(final CreateOrderModel model) {
        if (model == null) return null;

        final List<OrderItem> products = model.getProducts()
                .stream()
                .map(mapModelToDomain)
                .toList();

        return CreateOrder.issue(
                CustomerId.from(model.getCustomerId()),
                products
        );
    }
}
