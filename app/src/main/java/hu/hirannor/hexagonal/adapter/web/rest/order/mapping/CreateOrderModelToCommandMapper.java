package hu.hirannor.hexagonal.adapter.web.rest.order.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.CreateOrderModel;
import hu.hirannor.hexagonal.adapter.web.rest.orders.model.OrderedProductModel;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderItem;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;

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
